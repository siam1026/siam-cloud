# Siam-Cloud 项目面试问答手册

> 基于真实项目经验整理，每日背诵用

---

## 一、项目整体架构

### 1. 介绍一下你的项目

Siam-Cloud 是一个基于 Spring Cloud 的分布式外卖/商城微服务项目。前端包括 Vue 管理后台 + 微信小程序，后端拆分为 10 个独立微服务：user（用户）、merchant（商家）、rider（骑手）、goods（商品）、order（订单）、promotion（促销）、mall（积分商城）、util（工具服务）、gateway（网关）、monitor（监控）。

技术栈：Spring Boot 3.2.5 + JDK 17 + Spring Cloud 2023.0.1 + Nacos 2.x + Spring Cloud Gateway + MyBatis-Plus 3.5.5 + Seata + RocketMQ + ShardingSphere + Redis。

### 2. 你们服务为什么要拆成这么多？按什么原则拆的？

按业务域拆分，每个服务有自己的数据库和职责边界：
- **user**：用户注册、登录、会员信息、余额/积分、邀请关系
- **merchant**：商家入驻、店铺管理、提现
- **rider**：骑手接单、配送
- **order**：订单生命周期、支付回调、退款、物流
- **goods**：商品管理、分类、购物车
- **promotion**：优惠券、满减活动
- **mall**：积分商城（独立分库分表）
- **util**：通用能力（短信、文件上传、系统使用记录）
- **gateway**：统一入口、鉴权、限流、路由

拆分的好处：团队可以独立开发部署，订单量暴涨时只扩 order 服务，不影响其他模块。

### 3. 你们服务的调用链路是怎样的？

微信小程序/管理后台 -> Gateway（端口 8081） -> 各 Provider 服务。服务间通过 Feign 调用，比如订单服务调用用户服务的 `MemberFeignApi` 扣减余额，调用商品服务的 `GoodsFeignApi` 扣减库存。所有注册到 Nacos。

---

## 二、Spring Cloud Gateway

### 4. 为什么用 Gateway 不用 Zuul？

Zuul 1.x 是阻塞 IO 模型，基于 Servlet，Spring 官方已经放弃维护了。Spring Cloud Gateway 基于 Spring WebFlux + Reactor，非阻塞异步模型，性能更高。而且 Zuul 1.x 没有 Spring Boot 3 兼容版本，升级到 Spring Boot 3.2 必须替换。

### 5. Gateway 的路由是怎么配置的？

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-siam
          predicates:
            - Path=/api-user/**
          filters:
            - StripPrefix=1
```

`lb://` 表示通过负载均衡从 Nacos 获取服务实例。`StripPrefix=1` 表示转发时剥离第一段路径（如 `/api-user/rest/member/login` 转发为 `/rest/member/login`）。

### 6. 你们 Gateway 做了哪些全局过滤器？

- **AuthGlobalFilter**：检查请求是否携带有效 token（从 Authorization header 或 URL 参数获取），对白名单路径（登录、注册、支付回调、Swagger）放行，其余拦截
- 将 token 放入 `X-Auth-Token` 请求头传递给下游服务

### 7. `StripPrefix=0` 和 `StripPrefix=1` 有什么区别？你踩过什么坑？

`StripPrefix=0` 不剥离前缀，请求原封不动转发。`StripPrefix=1` 剥离第一段。

踩坑：最开始配了 `StripPrefix=0`，前端请求 `/api-rider/rest/actuator/health`，Gateway 原样转发给 rider 服务，rider 收到完整路径找不到对应 Controller，触发 `NoResourceFoundException`，被全局异常处理器包装成 HTTP 200 + `{"success":false,"message":"系统异常"}`。表面看是 200，但实际是错误响应。改成 `StripPrefix=1` 后转发为 `/rest/actuator/health`，正常匹配到 Controller。

### 8. Gateway 跨域怎么处理的？

Gateway 层统一处理 CORS，配置 `globalcors`：
```yaml
globalcors:
  corsConfigurations:
    '[/**]':
      allowedOriginPatterns: "*"
      allowedMethods: "*"
      allowedHeaders: "*"
      allowCredentials: true
```
下游服务不需要再单独配跨域。

### 9. Gateway 响应式模型和非响应式服务混用有什么问题？

Gateway 基于 Spring WebFlux（响应式），下游服务是传统的 Servlet 模型（WebMVC）。Gateway 的请求/响应是 `ServerHttpRequest`/`ServerWebExchange`，不能直接用 Servlet API。比如过滤器里获取请求体不能像 Servlet 那样 `request.getInputStream()`，要用 `exchange.getRequest().getBody()` 返回的 `Flux<DataBuffer>`。

### 10. 为什么 Gateway 需要单独加 `spring-cloud-starter-loadbalancer`？

Spring Cloud 2020+ 移除了 Netflix Ribbon，`lb://` 协议的负载均衡依赖 `spring-cloud-starter-loadbalancer`。Gateway 不依赖 `siam-common`，所以这个依赖必须单独加在 Gateway 的 pom.xml 里，否则启动后所有路由都返回 503。

---

## 三、Nacos 注册中心

### 11. 为什么选 Nacos 不选 Eureka？

- **Eureka 2.x 开源停止了**，Spring Cloud 官方也放弃了 Eureka 的后续支持
- Nacos 同时支持服务注册和配置中心，Eureka 只有注册中心
- Nacos 支持 AP 和 CP 两种模式，Eureka 只支持 AP
- Nacos 是阿里巴巴的，和 Spring Cloud Alibaba 生态无缝集成

### 12. Nacos 的服务发现流程是怎样的？

1. 服务启动时，`spring-cloud-starter-alibaba-nacos-discovery` 自动向 Nacos Server 注册自己的 IP、端口、健康状态
2. 客户端通过 Feign 调用服务时，`LoadBalancer` 从 Nacos 拉取服务实例列表
3. 默认使用轮询策略选择一个实例发起 HTTP 请求
4. Nacos 通过心跳机制（默认 5 秒）检测服务健康，15 秒无心跳标记不健康，30 秒剔除

### 13. 你们本地怎么启动 Nacos 的？

下载 Nacos Server 2.3.x ZIP，解压后执行 `startup.cmd -m standalone`（单机模式）。访问 `http://127.0.0.1:8848/nacos`，默认账号密码 `nacos/nacos`。

### 14. Nacos 和 Eureka 配置有什么不一样？

```yaml
# Eureka（旧）
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:9000/eureka/

# Nacos（新）
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
      config:
        server-addr: 127.0.0.1:8848
        file-extension: yaml
```

Java 代码层面：`@EnableEurekaClient` 改为 `@EnableDiscoveryClient`。

---

## 四、Feign 服务间调用

### 15. Feign 调用返回的 `BasicResult.getData()` 可能为 null，怎么处理？

踩坑经验：如果下游服务没启动、接口不存在或者返回空，`BasicResult.getData()` 就是 null。直接 `.getData()` 拆箱成 `int` 或 `BigDecimal` 会抛 `NullPointerException`。

解决方案：封装安全拆箱方法：
```java
private int safeInt(BasicResult<Integer> result) {
    if (result == null || result.getData() == null) return 0;
    return result.getData();
}

private BigDecimal safeBigDecimal(BasicResult<?> result) {
    if (result == null || result.getData() == null) return BigDecimal.ZERO;
    Object data = result.getData();
    if (data instanceof BigDecimal) return (BigDecimal) data;
    if (data instanceof Double) return BigDecimal.valueOf((Double) data);
    if (data instanceof Number) return BigDecimal.valueOf(((Number) data).doubleValue());
    return BigDecimal.ZERO;
}
```

实际项目中在 `StatisticsServiceImpl` 和 `PointsMallStatisticsServiceImpl` 里修复了 60+ 处这样的隐患。

### 16. Feign 的超时时间怎么配置？

在 `application.yml` 中配置：
```yaml
spring:
  cloud:
    openfeign:
      client:
        config:
          default:
            connect-timeout: 5000
            read-timeout: 10000
```

### 17. Feign 调用时怎么传递用户 token/登录态？

在 Gateway 的 `AuthGlobalFilter` 中，验证 token 后将其放入 `X-Auth-Token` 请求头。下游服务通过拦截器读取该 header 识别用户身份。

---

## 五、数据库与分库分表

### 18. 你们项目有几个数据库？

7 个数据库：
| 数据库 | 用途 |
|--------|------|
| `siam_cloud` | 核心服务（user、merchant、goods、order、promotion、rider、util） |
| `siam_cloud_0` | Order 分库节点 0 |
| `siam_cloud_1` | Order 分库节点 1 |
| `siam_cloud_mall` | 积分商城主库 |
| `siam_cloud_mall_0` | Mall 分库节点 0 |
| `siam_cloud_mall_1` | Mall 分库节点 1 |

### 19. ShardingSphere 的分片策略怎么配的？

数据库分片按 `member_id % 2` 路由：
```yaml
spring:
  shardingsphere:
    rules:
      sharding:
        tables:
          tb_order:
            actual-data-nodes: ds$->{0..1}.tb_order_$->{0..1}
            database-strategy:
              standard:
                sharding-column: member_id
                sharding-algorithm-name: database-inline
            table-strategy:
              standard:
                sharding-column: id
                sharding-algorithm-name: table-inline
        sharding-algorithms:
          database-inline:
            type: INLINE
            props:
              algorithm-expression: ds$->{member_id % 2}
          table-inline:
            type: INLINE
            props:
              algorithm-expression: tb_order_$->{id % 2}
```

主键生成用雪花算法：
```yaml
key-generate-strategy:
  column: id
  key-generator-name: snowflake
```

### 20. 分库分表后怎么做分页查询？

跨分片的分页查询性能很差，因为需要聚合所有分片的结果再排序。我们的做法是：
- 管理后台的分页查询一般不跨分片（按商家维度查询，商家 id 固定）
- 如果要查全局数据，限制最大页数（比如最多翻 100 页）
- 对于大数据量统计，用定时任务预聚合到统计表

### 21. MyBatis-Plus 从旧版升级到 3.5.5 有什么要注意的？

- `PaginationInterceptor` 改为 `MybatisPlusInterceptor` + `PaginationInnerInterceptor`
- `count()` / `selectCount()` 返回值从 `int` 改为 `long`
- Spring Boot 3.x 需要显式声明 `mybatis-spring` 3.x 版本（2.x 不兼容 Spring 6）

```xml
<properties>
    <mybatis-spring.version>3.0.4</mybatis-spring.version>
</properties>
```

---

## 六、Redis 与分布式锁

### 22. 你们项目里 Redis 用在了哪些场景？

1. **登录 Session 存储**：用户登录后 token 存入 Redis，设置 7 天过期
2. **支付回调幂等**：以订单号为 key 的 SETNX 分布式锁，防止微信重复回调
3. **短信验证码**：验证码存入 Redis，5 分钟过期
4. **购物车**：临时缓存
5. **网关限流**（设计层面）：基于 Redis 的令牌桶/计数器

### 23. 支付回调的幂等锁是怎么实现的？

```java
String lockKey = "pay:callback:lock:" + outTradeNo;
Boolean isLocked = stringRedisTemplate.opsForValue()
    .setIfAbsent(lockKey, "1", 3, TimeUnit.MINUTES);
if (Boolean.FALSE.equals(isLocked)) {
    log.warn("订单 {} 正在处理或已处理，触发幂等拦截", outTradeNo);
    // 直接回复 SUCCESS，避免微信继续重试
    resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code>...</xml>";
    return;
}
try {
    // 核心业务逻辑：判断订单状态 -> 更新订单 -> 增加余额
} finally {
    // 不主动删锁，保留至自然过期
    // 因为微信可能高并发重复发送同一笔回调
}
```

关键点：
- `setIfAbsent` 对应 Redis 的 `SETNX`，原子操作
- 锁过期时间 3 分钟，防止死锁
- `finally` 中不主动删锁，因为成功后短时间内同一订单不应该再处理
- 幂等拦截后直接回复 SUCCESS，微信收到成功就不会继续重试

### 24. Redis 密码配置踩过什么坑？

本地 Redis 启动时没加载 `redis.conf`（`redis-server.exe` 不带参数启动），所以实际是无密码模式。但应用配置中写了 `password: root`，导致连接时发送 AUTH 命令被拒，报错：`ERR AUTH <password> called without any password configured for the default user`。把应用配置的 password 清空就好了。

---

## 七、Seata 分布式事务

### 25. 你们项目用 Seata 做什么？

分布式事务，保证跨服务的操作要么全部成功要么全部回滚。比如用户下单场景：订单服务创建订单 -> 商品服务扣减库存 -> 促销服务核销优惠券。这三个操作分布在三个不同的服务/数据库，需要分布式事务保证一致性。

### 26. Seata 的 AT 模式原理是什么？

AT 模式（Automatic Transaction）是 Seata 默认的模式，无需改业务代码：
1. **一阶段**：Seata 拦截业务 SQL，解析 SQL 生成 undo log（前置镜像 + 后置镜像），然后执行业务 SQL，提交本地事务
2. **二阶段提交**：收到 TC 的提交指令，异步删除 undo log
3. **二阶段回滚**：收到 TC 的回滚指令，用 undo log 中的前置镜像恢复数据

关键依赖：`@GlobalTransactional` 注解，标记在全局事务的发起方。

### 27. Seata 的注册中心从 Eureka 迁到 Nacos 怎么做的？

之前 `seata.registry.type` 配的是 `eureka`，指向 `http://127.0.0.1:9000/eureka/`。Eureka 模块删除后，改为：

```yaml
seata:
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: 127.0.0.1:8848
      group: SEATA_GROUP
  config:
    type: nacos
    nacos:
      server-addr: 127.0.0.1:8848
      group: SEATA_GROUP
```

同时 `application-id` 从硬编码改为 `${spring.application.name}`。

---

## 八、RocketMQ 事务消息

### 28. 你们用 RocketMQ 做什么？

订单相关的事务消息和异步通知。比如支付成功后需要发送通知、更新统计等操作，用事务消息保证消息一定送达。

### 29. RocketMQ 事务消息的原理是什么？

分三个阶段：
1. **发送 Half 消息**：生产者先发送半消息（对消费者不可见）到 Broker
2. **执行本地事务**：执行本地业务逻辑（如更新订单状态）
3. **提交/回滚**：根据本地事务结果，向 Broker 发送 Commit 或 Rollback 指令
4. **回查机制**：如果 Broker 长时间没收到确认，会回调 `checkLocalTransaction` 方法检查本地事务状态

我们的实现：
```java
@Component
public class TransactionProducer {
    private TransactionMQProducer producer;
    // 事务组名
    producer.setTransactionListener(new OrderTransactionListener());
}
```

---

## 九、Spring Boot 3.x 升级

### 30. Spring Boot 2 升级到 3，遇到了哪些兼容问题？

**最核心的改动：**

1. **javax → jakarta 命名空间迁移**：`javax.servlet` → `jakarta.servlet`，`javax.validation` → `jakarta.validation`，`javax.annotation` → `jakarta.annotation`，`javax.websocket` → `jakarta.websocket`，`javax.mail` → `jakarta.mail`。但 `javax.crypto`、`javax.net.ssl`、`javax.imageio` 这些 Java SE 内置的不用改。影响 50+ 个文件。

2. **Springfox Swagger 不兼容**：Springfox 项目已停止维护，不兼容 Spring Boot 3。改用 SpringDoc OpenAPI：
   - 删除 `springfox-swagger2` + `springfox-swagger-ui`
   - 新增 `springdoc-openapi-starter-webmvc-ui` 2.5.0
   - 注解替换：`@Api` → `@Tag`，`@ApiOperation` → `@Operation`，`@ApiModel` → `@Schema`

3. **Spring Security 5 → 6**：`WebSecurityConfigurerAdapter` 被移除，改用 `SecurityFilterChain` Bean 模式。`antMatchers` 改为 `requestMatchers`。

4. **commons-lang 2.x 移除**：`org.apache.commons.lang` 改为 `org.apache.commons.lang3`。

5. **MyBatis-Spring 版本冲突**：Spring Boot 3.x 要求 `mybatis-spring` 3.x，但 MyBatis-Plus 3.5.5 的传递依赖默认是 2.1.2。需要在父 POM 中显式覆盖：
   ```xml
   <mybatis-spring.version>3.0.4</mybatis-spring.version>
   ```

### 31. JDK 8 升级到 JDK 17 有什么要注意的？

- `var` 关键字在 JDK 10+ 可用
- `switch` 表达式增强
- `record` 类型（JDK 16）
- 模块系统（JPMS）的强封装，反射访问内部 API 需要 `--add-opens`
- 垃圾回收器默认从 Parallel GC 改为 G1
- `Date` 构造器已废弃（但项目里还在用 `new Date("1970/1/1 00:00:00")`）
- `BigDecimal.ROUND_HALF_UP` 已废弃，建议用 `RoundingMode.HALF_UP`

### 32. Spring Boot 3 循环依赖默认禁止，怎么解决的？

Spring Boot 3 把 `allow-circular-references` 默认值改为 `false`。项目中有多处循环依赖，用 `@Lazy` 延迟注入打破循环：

**案例 1**：`OrderServiceImpl` ↔ `WxPayService`（互相注入）
**案例 2**：`RocketMQ_OrderServiceImpl` → `TransactionProducer` → `OrderTransactionListener` → `RocketMQ_OrderServiceImpl`（三角循环）
**案例 3**：`MemberServiceImpl` ↔ `MemberBillingRecordServiceImpl`（双向依赖）

```java
@Lazy
@Autowired
private WxPayService wxPayService;
```

另一种方案是配置 `spring.main.allow-circular-references=true`，但不推荐，掩盖了设计问题。

### 33. MongoDB 自动装配的问题怎么解决的？

`siams-common` 依赖了 `spring-boot-starter-data-mongodb`，所有 Provider 通过传递依赖引入了 MongoDB 自动装配。但本地没装 MongoDB，启动时报 `MongoSocketOpenException: Connection refused`。

解决：在每个 Provider 的 `@SpringBootApplication` 注解中排除 MongoDB 自动配置：

```java
@SpringBootApplication(scanBasePackages = "com.siam", exclude = {
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class
})
```

---

## 十、微信支付

### 34. 微信支付回调的完整流程是什么？

1. 用户在前端调起微信支付
2. 用户支付成功后，微信服务器异步回调我们的 `wxNotify` 接口
3. 回调内容是 XML 格式，解析后得到参数 Map
4. **验签**：用 `PayUtil.verify()` 验证签名，确保回调确实来自微信
5. **幂等检查**：以订单号为 key 获取 Redis 分布式锁，如果已锁定说明正在处理或已处理，直接回复 SUCCESS
6. **业务处理**：检查订单状态 -> 更新订单为已支付 -> 增加商家余额 -> 发放优惠券
7. **回复微信**：返回 XML 格式的 `<return_code>SUCCESS</return_code>`
8. **释放锁**：保留至自然过期

### 35. 微信支付验签的 Bug 是怎么修复的？

`PayUtil.verify()` 方法中签名原始串拼接错误：
```java
// 错误：少了 "&key=" 前缀
text = text + key;

// 正确：和 sign() 方法保持一致
text = text + "&key=" + key;
```

`sign()` 方法用的是 `text + "&key=" + key`，但 `verify()` 用的是 `text + key`，导致验签时计算的签名和微信返回的签名永远不匹配，所以之前代码被注释掉了。修复后恢复了验签逻辑。

### 36. 微信支付回调为什么要做幂等？

微信在以下场景会重复发送回调通知：
- 第一次回调超时未收到响应
- 网络抖动导致超时
- 微信自身重试机制

如果不做幂等，同一笔订单会被处理多次（重复加钱、重复发货）。我们用 Redis `SETNX` 实现幂等控制，同一订单同一时间只处理一次。

---

## 十一、WebSocket

### 37. 你们的 WebSocket 是怎么实现的？

用 `jakarta.websocket` 实现服务端长连接，用于实时推送（如订单状态变更通知骑手）。

```java
@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketBaseServer {
    private static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        sessionMap.put(userId, session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 消息路由：以 "TOUSER" 开头表示私聊
    }

    @OnClose
    public void onClose(Session session) {
        // 从 sessionMap 移除
    }
}
```

注意：从 `javax.websocket` 改为 `jakarta.websocket`（Spring Boot 3 要求）。

---

## 十二、异常处理与日志

### 38. 你们的全局异常处理器怎么设计的？

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(StoneCustomerException.class)
    public BasicResult handleCustomerException(StoneCustomerException e) {
        return BasicResult.failure(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public BasicResult handleBindException(BindException e) {
        return BasicResult.failure("参数校验失败：" + e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public BasicResult handleException(Exception e) {
        log.error("系统异常", e);
        return BasicResult.failure("系统异常，请稍后再试");
    }
}
```

踩坑：当路由配置错误时（如 `StripPrefix=0`），`NoResourceFoundException` 被全局异常处理器捕获，包装成 HTTP 200 的错误响应。这导致外部看是 200 但内容是错误信息，排查时容易误导。

### 39. `StoneCustomerException` 是什么？

自定义业务异常，继承 `RuntimeException`，用于抛出可预期且有明确提示信息的业务错误。比如"该店铺不存在"、"手机验证码错误"、"订单已过期"等。被全局异常处理器捕获后直接返回给用户。

---

## 十三、认证与授权

### 40. 你们项目的认证机制是怎样的？

基于 Token + Redis 的 Session 管理：

1. 用户登录成功后生成唯一 token
2. token 作为 key，用户信息作为 value 存入 Redis，设置 7 天过期
3. 后续请求携带 token（Authorization header 或 URL 参数）
4. Gateway 的 `AuthGlobalFilter` 验证 token 存在性（不验证有效性，交给下游服务）
5. 下游服务通过 `MemberSessionManager` 从 Redis 读取 Session，获取用户信息
6. 商家端有独立的 `MerchantSessionManager`

```java
public class TokenUtil {
    public static String getToken() {
        // 从请求上下文中获取 token
        // 优先级：header > cookie > URL parameter
    }
}
```

### 41. Gateway 的鉴权过滤器是怎么工作的？

```java
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = request.getURI().getPath();
        // 1. OPTIONS 请求直接放行（CORS 预检）
        // 2. 白名单路径放行（/login, /register, /pay, /notify 等）
        // 3. 从 header 或 query 中提取 token
        // 4. token 为空返回 401
        // 5. token 放入 X-Auth-Token header 传递给下游
    }
}
```

白名单：`/login`、`/register`、`/swagger`、`/v3/api-docs`、`/actuator`、`/notify`、`/callback`、`/pay`、`/Pay`、`/shop`。

---

## 十四、业务场景题

### 42. 用户下单的完整流程是怎样的？

1. 用户从商品详情页加入购物车
2. 进入结算页，选择地址、优惠券
3. 调用 `order-siam` 创建订单
4. 选择支付方式（微信支付 / 余额支付 / 积分支付）
5. 微信支付：调用微信统一下单 API，返回 prepay_id，前端调起支付
6. 支付成功后微信回调 `wxNotify` 接口
7. 验签 + 幂等检查
8. 更新订单状态为已支付
9. 扣减商品库存
10. 核销优惠券
11. 通知商家（WebSocket 推送）
12. 分配骑手（根据距离和骑手状态）

### 43. 商家入驻的流程是怎样的？

1. 商家通过微信小程序或 H5 页面提交入驻申请（营业执照、身份证照片、店铺照片等）
2. 创建 `tb_shop` 记录，`audit_status=1`（待审核）
3. 同时创建 `tb_merchant` 记录
4. 管理后台在"入驻审核"页面看到待审核记录
5. 管理员审核通过：`audit_status=2`，店铺状态设为"待上架"
6. 管理员上架：`status=2`，店铺对消费者可见
7. 审核不通过：`audit_status=3`，填写审核意见

### 44. 退款流程是怎样的？

1. 用户发起退款申请
2. 订单状态改为"退款中"
3. 商家审核退款申请
4. 审核通过后调用微信退款 API
5. 微信退款回调 `refundSuccessNotify`
6. 验签 + 更新退款状态
7. 恢复商品库存
8. 退还用户余额/积分

---

## 十五、运维与部署

### 45. Actuator 为什么要收敛暴露的端点？

默认 `include: "*"` 会暴露所有端点，包括：
- `/env` — 环境变量（含数据库密码）
- `/heapdump` — 堆转储（含内存数据）
- `/threaddump` — 线程堆栈
- `/configprops` — 配置属性

这些都是敏感信息，收敛为只暴露必要端点：
```yaml
management:
  endpoints:
    web:
      exposure:
        include: "health,prometheus,info"
```

### 46. Actuator 邮件健康检查的问题怎么解决的？

Actuator 默认对配置的邮件服务做健康检查。我们配了 126 SMTP 服务器，但密码可能过期或配置有误，导致 `jakarta.mail.AuthenticationFailedException: 535 Error: authentication failed`，这个错误会阻止服务启动。

解决：在 `management.health` 中关闭邮件健康检查：
```yaml
management:
  health:
    mail:
      enabled: false
```

### 47. 本地开发环境涉及哪些中间件？

| 中间件 | 端口 | 用途 |
|--------|------|------|
| MySQL | 3306 | 主数据库 |
| Redis | 6379 | 缓存/Session/分布式锁 |
| Nacos | 8848 | 服务注册/配置中心 |
| Gateway | 8081 | 网关入口 |
| Seata | 8091 | 分布式事务（部署中） |

---

## 十六、项目难点 & 亮点

### 48. 你做这个项目最大的收获/难点是什么？

**最大的收获是完成了一次完整的 Spring Boot 2 → 3 技术栈升级。** 这不只是一个版本号的变更，涉及：
- `javax` 到 `jakarta` 的命名空间迁移（50+ 文件）
-  Zuul 到 Gateway 的架构替换
- Eureka 到 Nacos 的注册中心替换
- Springfox 到 SpringDoc 的文档框架替换
- 60+ 处 Feign 调用空指针隐患的排查和修复
- 支付验签 Bug 的根因分析和修复
- 分布式锁的正确实现

**最大的难点**是升级后服务能编译但不能运行，需要在运行时一层层排查：循环依赖、MongoDB 自动装配、mybatis-spring 版本冲突、Redis 密码配置不一致等。每个问题都需要深入理解 Spring Boot 的自动装配机制。

### 49. 如果订单量暴增 10 倍，你的架构怎么优化？

1. **水平扩展**：order 服务多实例部署，通过 Nacos 负载均衡
2. **分库分表**：现有 2 个分片可能不够，扩展到 8 个或 16 个
3. **异步化**：非核心操作（如发短信通知、写操作日志）异步执行
4. **缓存**：热点商品数据加 Redis 缓存，减少 DB 压力
5. **限流降级**：Gateway 层加限流，超过阈值时拒绝非核心请求
6. **读写分离**：MySQL 主从，读走从库，写走主库

### 50. 你觉得这个项目的不足在哪里？

1. **安全方面**：Token 验证在 Gateway 层只做存在性检查，有效性验证由各服务自行处理，存在不一致的风险。应该在 Gateway 层统一验证。
2. **监控方面**：虽然有 Actuator 和 Monitor，但缺少完整的链路追踪（如 SkyWalking、Zipkin），出了问题排查困难。
3. **测试覆盖**：项目基本没有单元测试和集成测试，改动后依赖手动验证。
4. **配置管理**：配置文件还是本地管理，没有接入 Nacos Config 的动态配置能力。
5. **日志管理**：日志分散在各个服务的本地文件，没有统一的 ELK 日志中心。

---

## 每日背诵建议

| 日期 | 背诵范围 | 重点 |
|------|---------|------|
| Day 1 | 一~二节（项目架构 + Gateway） | 能口述服务拆分原则、Gateway 路由配置、StripPrefix 踩坑 |
| Day 2 | 三~四节（Nacos + Feign） | 服务发现流程、BasicResult 空指针修复 |
| Day 3 | 五~六节（分库分表 + Redis） | ShardingSphere 分片策略、支付幂等锁实现 |
| Day 4 | 七~八节（Seata + RocketMQ） | AT 模式原理、事务消息三阶段 |
| Day 5 | 九节（Spring Boot 3 升级） | javax→jakarta、循环依赖、mybatis-spring 冲突 |
| Day 6 | 十~十一节（微信支付 + WebSocket） | 验签 Bug 根因、回调完整流程 |
| Day 7 | 十二~十六节（综合） | 认证机制、业务流程、运维配置、项目难点 |
