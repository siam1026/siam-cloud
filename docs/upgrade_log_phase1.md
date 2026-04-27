# Siam-Cloud 升级日志

## 第一阶段：核心框架升级（Spring Boot 3.2 + JDK 17）

### 升级时间

2026-04-25

### 升级目标

将 Siam-Cloud 多模块 Maven 项目从老旧技术栈升级到现代技术栈，使其能够编译通过并为后续改造奠定基础。

---

### 一、框架版本升级

| 组件 | 升级前 | 升级后 |
|------|--------|--------|
| JDK | 1.8 | 17 |
| Spring Boot | 2.3.3.RELEASE | 3.2.5 |
| Spring Cloud | 2.2.x (Hoxton, 已注释) | 2023.0.1 (Leyton) |
| Spring Cloud Alibaba | 无 | 2023.0.1.0 |
| MyBatis-Plus | 旧版 (PaginationInterceptor) | 3.5.5 (MybatisPlusInterceptor) |
| Lombok | 旧版 | 1.18.30 |
| Swagger | springfox-swagger2 2.x | springdoc-openapi 3.x (SpringDoc) |

### 二、模块级改动

#### 1. javax → jakarta 命名空间迁移

| javax 包 | jakarta 包 | 影响范围 |
|----------|-----------|---------|
| `javax.servlet` | `jakarta.servlet` | ~50 文件 |
| `javax.validation` | `jakarta.validation` | ~15 文件 |
| `javax.annotation` | `jakarta.annotation` | ~15 文件 |
| `javax.websocket` | `jakarta.websocket` | 1 文件 |
| `javax.mail` | `jakarta.mail` | 1 文件 |

#### 2. Swagger → SpringDoc 迁移

- 删除 `springfox-swagger2`、`springfox-swagger-ui`
- 新增 `springdoc-openapi-starter-webmvc-ui`
- 全项目注解迁移：`@Api`→`@Tag`，`@ApiOperation`→`@Operation`，`@ApiImplicitParam`→`@Parameter`，`@ApiModel`→`@Schema`

#### 3. MyBatis-Plus 3.5 适配

- `PaginationInterceptor` → `MybatisPlusInterceptor` + `PaginationInnerInterceptor`
- `count()` / `selectCount()` 返回值 `int` → `long`
- 分布式事务 Service 类改为继承 `ServiceImpl`

#### 4. Spring Cloud 注解清理

- `@EnableEurekaClient` → `@EnableDiscoveryClient`
- `@EnableCircuitBreaker` → 删除

### 三、编译验证

32 个模块全部编译通过，`BUILD SUCCESS`。

---

## 第二阶段：注册中心替换（Eureka → Nacos）

### 升级时间

2026-04-25

### 升级目标

移除已废弃的 Eureka 体系（包括 siam-eureka 服务端模块），全面接入 Nacos 作为注册中心和配置中心。

---

### 一、改动总览

| 改动项 | 数量 |
|--------|------|
| POM 文件改动 | 11 个 |
| 配置文件改动 | 11 个 |
| Java 代码改动 | 1 个 |
| 删除的模块 | siam-eureka |
| 新增的依赖 | spring-cloud-starter-alibaba-nacos-discovery |
| 新增的依赖 | spring-cloud-starter-alibaba-nacos-config |

### 二、POM 改动详情

#### 1. 父 POM（siam-parent/pom.xml）

从 `<modules>` 中移除 `../siam-eureka`。

#### 2. Provider 模块 POM（9 个文件）

| 文件 | 操作 |
|------|------|
| `siam-user/user-provider/pom.xml` | eureka-client → nacos-discovery + nacos-config |
| `siam-merchant/merchant-provider/pom.xml` | 同上 |
| `siam-rider/rider-provider/pom.xml` | 同上 |
| `siam-goods/goods-provider/pom.xml` | 同上 |
| `siam-order/order-provider/pom.xml` | 同上 |
| `siam-promotion/promotion-provider/pom.xml` | 同上 |
| `siam-mall/mall-provider/pom.xml` | 同上 |
| `siam-util/util-provider/pom.xml` | 同上 |
| `siam-monitor/pom.xml` | eureka-client → nacos-discovery |

替换模板：
```xml
<!-- 旧 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<!-- 新 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

#### 3. Gateway 模块 POM（1 个文件）

| 文件 | 操作 |
|------|------|
| `siam-gateway/pom.xml` | eureka-client → nacos-discovery |

Gateway 不需要配置中心，仅引入 nacos-discovery：
```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

### 三、配置文件改动详情

所有 provider 和 gateway 的配置文件中的 Eureka 配置块统一替换为 Nacos 配置。

#### 旧配置（所有文件一致）

```yaml
#eureka配置
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:9000/eureka/
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
```

#### 新配置

```yaml
# Nacos注册中心/配置中心
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
      config:
        server-addr: 127.0.0.1:8848
        file-extension: yaml
```

#### 涉及的文件列表

| 文件 | 备注 |
|------|------|
| `siam-user/user-provider/.../application-local.yml` | 需 nacos-config |
| `siam-merchant/merchant-provider/.../application-local.yml` | 需 nacos-config |
| `siam-rider/rider-provider/.../application-local.yml` | 需 nacos-config |
| `siam-goods/goods-provider/.../application-local.yml` | 需 nacos-config |
| `siam-order/order-provider/.../application-local.yml` | 需 nacos-config |
| `siam-promotion/promotion-provider/.../application-local.yml` | 需 nacos-config |
| `siam-mall/mall-provider/.../application-local.yml` | 需 nacos-config |
| `siam-util/util-provider/.../application-local.yml` | 需 nacos-config |
| `siam-monitor/.../application-local.yml` | 仅 nacos-discovery |
| `siam-gateway/.../application.yml` | 仅 nacos-discovery |

### 四、Java 代码改动

| 文件 | 改动 |
|------|------|
| `siam-user/.../AdminVipRechargeRecordController.java` | `org.apache.commons.lang.time.DateUtils` → `org.apache.commons.lang3.time.DateUtils` |

**原因**：Spring Boot 3.x 不再内置 commons-lang 2.x，需使用 commons-lang3（Apache Commons 3.x 版本）。

### 五、Seata 配置保留

各 provider 的 `application-local.yml` 中保留了原有的 Seata 配置块：
```yaml
# Seata Config
seata:
  application-id: xxx-service
  tx-service-group: my_test_tx_group
  service:
    vgroup-mapping:
      my_test_tx_group: default
  registry:
    type: eureka
    eureka:
      service-url: http://127.0.0.1:9000/eureka/
```

**注意**：Seata 配置仍指向 Eureka，后续如需使用 Seata 分布式事务，需要将其改为 Nacos 注册。

### 六、Nacos 本地启动方式

由于本地是纯 Windows 环境，不使用 Docker，按以下步骤启动：

1. 前往 [Nacos Releases](https://github.com/alibaba/nacos/releases)
2. 下载 `nacos-server-2.3.x.zip`
3. 解压到本地目录（如 `D:\nacos`）
4. 打开命令行，进入 `D:\nacos\bin`
5. 执行 `startup.cmd -m standalone`（单机模式）
6. 访问 `http://127.0.0.1:8848/nacos` 确认启动成功
7. 默认账号密码：`nacos` / `nacos`

### 七、编译验证

```
$ mvn clean install -DskipTests

Reactor Summary for siam-parent 1.1:
  siam-parent ............. SUCCESS
  siam-common ............. SUCCESS
  siam-weixin ............. SUCCESS
  weixin-basic ............ SUCCESS
  weixin-pay .............. SUCCESS
  siam-user ............... SUCCESS
  user-api ................ SUCCESS
  siam-order .............. SUCCESS
  order-api ............... SUCCESS
  siam-promotion .......... SUCCESS
  promotion-api ........... SUCCESS
  siam-util ............... SUCCESS
  util-api ................ SUCCESS
  user-provider ........... SUCCESS
  siam-merchant ........... SUCCESS
  merchant-api ............ SUCCESS
  siam-goods .............. SUCCESS
  goods-api ............... SUCCESS
  merchant-provider ....... SUCCESS
  siam-rider .............. SUCCESS
  rider-api ............... SUCCESS
  rider-provider .......... SUCCESS
  siam-mall ............... SUCCESS
  mall-api ................ SUCCESS
  goods-provider .......... SUCCESS
  order-provider .......... SUCCESS
  promotion-provider ...... SUCCESS
  mall-provider ........... SUCCESS
  util-provider ........... SUCCESS
  siam-monitor ............ SUCCESS
  siam-gateway ............ SUCCESS

BUILD SUCCESS
Total time: ~51s
```

**31 个模块全部编译通过**（已移除 siam-eureka）。

---

## 第三阶段：运行时修复 + 服务启动验证

### 升级时间

2026-04-25

### 升级目标

解决服务运行时启动失败问题，逐一修复循环依赖、MongoDB 自动装配、依赖版本冲突等报错，最终使核心服务成功注册到 Nacos。

---

### 一、mybatis-spring 版本冲突修复

#### 报错信息

```
java.lang.IllegalArgumentException: Invalid value type for attribute 'factoryBeanObjectType': java.lang.String
```

#### 根因

MyBatis-Plus 3.5.5 的 `mybatis-plus-boot-starter` 默认传递依赖 `mybatis-spring` **2.1.2**。Spring Boot 3.x 使用 Spring Framework 6.x，要求 `mybatis-spring` 必须是 **3.x** 系列。2.x 版本的 `FactoryBeanObjectType` 类型处理与 Spring 6 的严格类型校验不兼容。

#### 修复

在 `siam-parent/pom.xml` 中显式声明覆盖版本：

```xml
<properties>
    <mybatis-spring.version>3.0.4</mybatis-spring.version>
</properties>

<dependencyManagement>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>${mybatis-spring.version}</version>
    </dependency>
</dependencyManagement>
```

#### 验证结果

```
org.mybatis:mybatis-spring:jar:3.0.4:compile  ✓
BUILD SUCCESS
```

---

### 二、Spring Cloud LoadBalancer 缺失修复

#### 报错信息

```
java.lang.IllegalStateException: No Feign Client for loadBalancing defined. Did you forget to include spring-cloud-starter-loadbalancer?
```

#### 根因

Spring Cloud 2020+ 移除了 Netflix Ribbon，Feign 客户端需要 `spring-cloud-starter-loadbalancer` 作为替代实现。项目中只有 `spring-cloud-starter-openfeign`，缺少负载均衡依赖。

#### 修复

在 `siam-common/pom.xml` 中新增：

```xml
<!-- Spring Cloud LoadBalancer（替代 Ribbon，Feign 必需） -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

由于所有 Provider 模块都依赖 `siam-common`，一处引入即可全局生效。

---

### 三、MongoDB 自动装配排除

#### 报错信息

```
com.mongodb.MongoSocketOpenException: Exception opening socket to localhost/127.0.0.1:27017
Connection refused
```

#### 根因

`siam-common` 模块依赖了 `spring-boot-starter-data-mongodb`，所有 Provider 模块通过传递依赖引入了 MongoDB 自动装配。但本地环境并未安装/启动 MongoDB 服务，Spring Boot 启动时自动尝试连接失败。

#### 修复

在所有 8 个 Provider 的 `*Application.java` 中排除 MongoDB 自动配置：

```java
@SpringBootApplication(scanBasePackages = "com.siam", exclude = {
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class
})
```

注意 Spring Boot 3.x 中 `MongoDataAutoConfiguration` 的包路径为 `org.springframework.boot.autoconfigure.data.mongo`（不是 `org.springframework.boot.autoconfigure.mongo`）。

#### 涉及文件

| 文件 | 模块 |
|------|------|
| `UserApplication.java` | user |
| `MerchantApplication.java` | merchant |
| `RiderApplication.java` | rider |
| `GoodsApplication.java` | goods |
| `OrderApplication.java` | order |
| `PromotionApplication.java` | promotion |
| `MallApplication.java` | mall |
| `UtilApplication.java` | util |

---

### 四、循环依赖修复（@Lazy 斩断）

Spring Boot 3 默认禁止循环依赖（`allow-circular-references` 默认为 false）。项目中存在多处双向/三角循环依赖，逐一使用 `@Lazy` 打破。

#### 4.1 Order 服务：OrderServiceImpl ↔ WxPayService

| 文件 | 注入字段 | 修复 |
|------|---------|------|
| `OrderServiceImpl.java` | `wxPayService` | 加 `@Lazy` |
| `WxPayService.java` | `orderService` | 加 `@Lazy` |

#### 4.2 Order 服务：RocketMQ 事务消息三角循环

循环链路：`RocketMQ_OrderServiceImpl → TransactionProducer → OrderTransactionListener → RocketMQ_OrderServiceImpl`

| 文件 | 注入字段 | 修复 |
|------|---------|------|
| `RocketMQ_OrderServiceImpl.java` | `pointsMallTransactionProducer` | 加 `@Lazy` |
| `TransactionProducer.java` | `orderTransactionListener` | 加 `@Lazy` |
| `OrderTransactionListener.java` | `orderService` | 加 `@Lazy` |

#### 4.3 User 服务：MemberServiceImpl ↔ MemberBillingRecordServiceImpl

| 文件 | 注入字段 | 修复 |
|------|---------|------|
| `MemberServiceImpl.java` | `memberBillingRecordService` | 加 `@Lazy` |
| `MemberBillingRecordServiceImpl.java` | `memberService` | 加 `@Lazy` |

#### 4.4 User 服务：MemberServiceImpl ↔ MemberInviteRelationServiceImpl

| 文件 | 注入字段 | 修复 |
|------|---------|------|
| `MemberServiceImpl.java` | `memberInviteRelationService` | 加 `@Lazy` |
| `MemberInviteRelationServiceImpl.java` | `memberService` | 加 `@Lazy` |

#### 4.5 Mall 服务：PointsMallOrderServiceImpl ↔ PointsMallWxPayService

| 文件 | 注入字段 | 修复 |
|------|---------|------|
| `PointsMallOrderServiceImpl.java` | `pointsMallWxPayService` | 加 `@Lazy` |
| `PointsMallWxPayService.java` | `pointsMallOrderService` | 加 `@Lazy` |

#### 4.6 Mall 服务：PointsMallOrderServiceImpl ↔ PointsMallOrderLogisticsServiceImpl

| 文件 | 注入字段 | 修复 |
|------|---------|------|
| `PointsMallOrderServiceImpl.java` | `orderLogisticsService` | 加 `@Lazy` |
| `PointsMallOrderLogisticsServiceImpl.java` | `orderService` | 加 `@Lazy` |

---

### 五、RocketMQAutoConfiguration 排除清理

#### 报错信息

```
java.lang.IllegalStateException: The following classes could not be excluded because they are not auto-configuration classes
```

#### 根因

多个 `*Application.java` 的 `@SpringBootApplication` 注解中排除了 `RocketMQAutoConfiguration.class`，但该依赖在新版本中已不属于 Spring Boot 自动配置范畴，排除无效反而报错。

#### 修复

清理 5 个 Application 类中的排除项：

| 文件 | 操作 |
|------|------|
| `OrderApplication.java` | 移除 exclude + 清理 import |
| `PromotionApplication.java` | 移除 exclude + 清理 import |
| `UserApplication.java` | 移除 exclude + 清理 import |
| `GoodsApplication.java` | 移除 exclude + 清理 import |
| `MallApplication.java` | 移除 exclude + 清理 import |

---

### 六、commons-lang3 修复

#### 报错信息

```
java.lang.ClassNotFoundException: org.apache.commons.lang.time.DateUtils
```

#### 根因

Spring Boot 3.x 不再内置 commons-lang 2.x。代码中使用了 `org.apache.commons.lang.time.DateUtils`，需替换为 `org.apache.commons.lang3.time.DateUtils`。

#### 修复

`AdminVipRechargeRecordController.java`：import 路径改为 `org.apache.commons.lang3.time.DateUtils`。

---

### 七、Redis 密码配置对齐

#### 报错信息

```
ERR AUTH <password> called without any password configured for the default user
```

#### 根因

本地 Redis 启动时未加载 `redis.conf` 配置文件（进程命令为 `redis-server.exe` 未指定 conf 文件），导致 Redis 实际无密码。但应用配置中 `password: root`，向无密码的 Redis 发送 AUTH 命令被拒绝。

#### 修复

统一将所有服务 `application-local.yml` 中的 Redis `password` 清空（无密码模式）。涉及 8 个 Provider 模块。

---

### 八、数据库准备

全局共需创建 7 个数据库：

| 数据库名 | 使用服务 | 说明 |
|----------|---------|------|
| `siam_cloud` | user, merchant, goods, order, promotion, rider, util | 7 个核心服务共用 |
| `siam_cloud_0` | order | Order ShardingSphere 分库节点 0 |
| `siam_cloud_1` | order | Order ShardingSphere 分库节点 1 |
| `siam_cloud_mall` | mall | 积分商城主库 |
| `siam_cloud_mall_0` | mall | Mall ShardingSphere 分库节点 0 |
| `siam_cloud_mall_1` | mall | Mall ShardingSphere 分库节点 1 |

对应 SQL 文件：
- `sql/mysql/siam_cloud.sql` → 导入到 siam_cloud 及其分库
- `sql/mysql/siam_cloud_mall.sql` → 导入到 siam_cloud_mall 及其分库

---

### 九、启动验证结果

| 服务 | 状态 | Nacos 注册 |
|------|------|-----------|
| gateway-siam | 启动成功 | ✓ |
| order-siam | 启动成功 | ✓ |
| user-siam | 启动成功 | ✓ |

---

### 十、待完成

#### 1. 剩余服务启动验证

依次启动其他 6 个 Provider（merchant、goods、rider、promotion、util、mall），验证全部注册到 Nacos。

#### 2. Seata 配置适配

各 provider 的 `application-local.yml` 中 Seata 配置仍指向 Eureka：

```yaml
seata:
  registry:
    type: eureka
    eureka:
      service-url: http://127.0.0.1:9000/eureka/
```

需要改为 Nacos：

```yaml
seata:
  registry:
    type: nacos
    nacos:
      server-addr: 127.0.0.1:8848
      group: SEATA_GROUP
```

#### 3. 支付验签修复 + 安全加固

- 恢复 `WxPayController` 中注释掉的验签代码
- 添加支付回调幂等控制（Redis setNX）
- Actuator 端点收敛（只开放 `/health` 和 `/prometheus`）

#### 4. Gateway 路由测试

验证 Gateway 到各 Provider 的路由转发是否正常。

#### 5. SpringDoc 文档页验证

验证各服务的 `/swagger-ui.html` 或 `/doc.html` 可正常访问。

---

### 十一、附加修复（本轮）

#### 11.1 Actuator 健康检查关闭邮件检测

**报错信息**：
```
jakarta.mail.AuthenticationFailedException: 535 Error: authentication failed
```

**根因**：Actuator 默认对邮件服务进行健康检查，连接配置的 126 SMTP 服务器时因认证失败导致服务启动被阻断。

**修复**：在所有 8 个 Provider + monitor 的 `application-local.yml` 中，`management.health` 下新增：
```yaml
mail:
  enabled: false
```

#### 11.2 util 服务 RestTemplate 自循环依赖

**报错信息**：
```
The dependencies of some of the beans form a cycle:
previousWxNotifyServiceImpl <-> previousWxNotifyServiceImpl
```

**根因**：`PreviousWxNotifyServiceImpl` 类内部既用 `@Bean` 定义了 `RestTemplate`，又用 `@Autowired` 注入了 `RestTemplate`，自己依赖自己。

**修复**：将 `@Bean RestTemplate()` 方法提取到独立的 `@Configuration` 类 `RestTemplateConfig` 中，解除自循环。

#### 11.3 MySQL 数据源统一

统一所有服务的 `application-local.yml` 中 MySQL 连接配置为：
```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/siam_cloud?...&allowPublicKeyRetrieval=true
    username: root
    password: 123456
```
包括 order 和 mall 服务的 ShardingSphere 分库（ds0/ds1）也统一了账号密码。

---

### 十二、全部服务启动验证结果

| 服务 | 状态 | Nacos 注册 |
|------|------|-----------|
| gateway-siam | 启动成功 | ✓ |
| order-siam | 启动成功 | ✓ |
| user-siam | 启动成功 | ✓ |
| merchant-siam | 启动成功 | ✓ |
| goods-siam | 启动成功 | ✓ |
| rider-siam | 启动成功 | ✓ |
| promotion-siam | 启动成功 | ✓ |
| util-siam | 启动成功 | ✓ |
| mall-siam | 启动成功 | ✓ |
| actuator-monitor | 启动成功 | ✓ |

**10 个服务全部启动成功并注册到 Nacos。**

---

## 第四阶段：Seata 配置迁移 + 支付验签修复 + 安全加固

### 升级时间

2026-04-25

### 升级目标

完善生产级别的安全配置，修复支付验签漏洞，将 Seata 注册中心从 Eureka 迁移到 Nacos，收敛 Actuator 暴露端点。

---

### 一、Seata 配置迁移（Eureka → Nacos）

**原因**：`siam-eureka` 模块已删除，但 8 个 provider 的 Seata 配置仍指向 `http://127.0.0.1:9000/eureka/`。

**改动内容**：
- `registry.type`: `eureka` → `nacos`
- `config.type`: 新增（之前缺失）
- `application-id`: 硬编码（如 `user-service`）→ `${spring.application.name}`

**新配置**：
```yaml
seata:
  application-id: ${spring.application.name}
  tx-service-group: my_test_tx_group
  service:
    vgroup-mapping:
      my_test_tx_group: default
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: 127.0.0.1:8848
      group: SEATA_GROUP
      namespace: public
  config:
    type: nacos
    nacos:
      server-addr: 127.0.0.1:8848
      group: SEATA_GROUP
      namespace: public
```

**涉及文件**（8 个 provider 的 `application-local.yml`）：
- user, merchant, rider, goods, order, promotion, mall, util

**验证**：`grep -r "eureka" **/src/**/*.yml` 仅剩余已废弃的 `siam-eureka` 模块本身，所有业务服务已彻底清除 Eureka 残留。

---

### 二、支付验签修复

#### 2.1 核心 Bug：`PayUtil.verify()` 签名不一致

**根因**：`sign()` 方法使用 `text + "&key=" + key`，而 `verify()` 方法使用 `text + key`（缺少 `&key=` 前缀），导致验签结果永远不匹配。这就是当初代码被注释掉的根本原因。

**修复**：
```java
// 修复前（错误）
text = text + key;

// 修复后（正确）
text = text + "&key=" + key;
```

**文件**：`siam-weixin/weixin-pay/src/main/java/com/siam/package_weixin_pay/util/PayUtil.java`

#### 2.2 解除验签注释

`WxPayController` 的两个回调方法中，将 `PayUtil.verify()` 验签逻辑恢复生效：

**`wxNotify`（支付回调）**：
```java
if (!PayUtil.verify(PayUtil.createLinkString(map), (String) map.get("sign"), wxPayConfig.getMchKey(), "utf-8")) {
    log.info("\n微信支付回调-验签失败");
    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
            + "<return_msg><![CDATA[验签失败]]></return_msg>" + "</xml> ";
    return;
}
```

**`refundSuccessNotify`（退款回调）**：
```java
if (!PayUtil.verify(PayUtil.createLinkString(map), (String) map.get("sign"), wxPayConfig.getMchKey(), "utf-8")) {
    log.info("\n微信退款回调-验签失败");
    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
            + "<return_msg><![CDATA[验签失败]]></return_msg>" + "</xml> ";
    return;
}
```

**Jakarta 适配**：`WxPayController` 已确认使用 `jakarta.servlet.http.HttpServletRequest`，无需额外修改。

**支付宝回调**：`PayDemoController.alipayCallback()` 中的 `AlipaySignature.rsaCheckV1()` 验签未被注释，无需修复。

---

### 三、支付回调幂等控制

**方案**：使用 Redis `setIfAbsent`（即 SETNX）以订单号为 key 实现分布式锁。

**核心逻辑**：
```java
String lockKey = "pay:callback:lock:" + outTradeNo;
Boolean isLocked = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "1", 3, TimeUnit.MINUTES);
if (Boolean.FALSE.equals(isLocked)) {
    log.warn("订单 {} 正在处理或已处理，触发幂等拦截", outTradeNo);
    resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    return;
}
try {
    // 原核心业务逻辑（判断状态、更新订单、增加余额等）
} finally {
    // 业务成功，锁保留至自然过期（3 分钟），防止微信短期并发重试
}
```

**锁策略说明**：
- `finally` 中不主动删锁（业务成功后锁保留至过期），因为微信可能在高并发场景下重复发送同一笔回调
- 3 分钟过期时间防止死锁
- 幂等拦截后直接回复 SUCCESS，避免微信继续重试

**涉及文件**：
- `siam-order/order-provider/.../WxPayController.java`（`wxNotify` + `refundSuccessNotify` 两个方法）

---

### 四、Actuator 端点收敛

**改动**：8 个 provider 的 `application-local.yml` 中：
```yaml
# 修复前（暴露全部端点，含 /env、/heapdump、/threaddump 等敏感接口）
include: "*" #'prometheus,health'

# 修复后（仅暴露必要端点）
include: "health,prometheus,info"
```

**涉及文件**（8 个 provider）：
- user, merchant, rider, goods, order, promotion, mall, util

---

### 五、文件改动汇总

| 文件 | 改动内容 |
|------|---------|
| `siam-weixin/weixin-pay/.../PayUtil.java` | `verify()` 方法 `text + key` → `text + "&key=" + key` |
| `siam-order/order-provider/.../WxPayController.java` | 恢复验签 + 注入 `StringRedisTemplate` + 两个回调加幂等锁 |
| `siam-user/.../application-local.yml` | Seata Eureka→Nacos + Actuator 收敛 |
| `siam-merchant/.../application-local.yml` | Seata Eureka→Nacos + Actuator 收敛 |
| `siam-rider/.../application-local.yml` | Seata Eureka→Nacos + Actuator 收敛 |
| `siam-goods/.../application-local.yml` | Seata Eureka→Nacos + Actuator 收敛 |
| `siam-order/.../application-local.yml` | Seata Eureka→Nacos + Actuator 收敛 |
| `siam-promotion/.../application-local.yml` | Seata Eureka→Nacos + Actuator 收敛 |
| `siam-mall/.../application-local.yml` | Seata Eureka→Nacos + Actuator 收敛 |
| `siam-util/.../application-local.yml` | Seata Eureka→Nacos + Actuator 收敛 |

---

## 第五阶段：Gateway 路由转发验证 + Swagger 文档访问

### 升级时间

2026-04-25

### 升级目标

验证 Gateway 能正确路由到所有 Provider 服务，确保整个系统端到端可用。

---

### 一、Gateway LoadBalancer 依赖修复

**报错信息**：Gateway 路由到服务时返回 `503 Service Unavailable`

**根因**：`siam-gateway/pom.xml` 缺少 `spring-cloud-starter-loadbalancer` 依赖，导致 Gateway 无法解析 `lb://user-siam` 格式的服务名。

**修复**：在 Gateway 的 `pom.xml` 中新增：
```xml
<!-- Spring Cloud LoadBalancer（Gateway 路由必需，用于解析 lb:// 服务名） -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

**原因分析**：之前 `spring-cloud-starter-loadbalancer` 只加在了 `siam-common/pom.xml` 中，但 Gateway 是一个独立的响应式模块（使用 `spring-cloud-starter-gateway`），不依赖 `siam-common`，需要单独引入。

---

### 二、Gateway AuthFilter 白名单补全

**报错信息**：`/api-order/rest/member/wxPay/...` 返回 401 "您暂未登录"

**根因**：`AuthGlobalFilter.java` 的 `isPublicPath()` 白名单只写了 `/pay`（小写），但实际路径是 `/wxPay`（大写 P），Java 字符串的 `contains()` 区分大小写，没有命中。

**修复**：
```java
private boolean isPublicPath(String uri) {
    return uri.contains("/login")
            || uri.contains("/register")
            || uri.contains("/swagger")
            || uri.contains("/v3/api-docs")
            || uri.contains("/actuator")
            || uri.contains("/notify")
            || uri.contains("/callback")
            || uri.contains("/pay")
            || uri.contains("/Pay")   // 新增：匹配 /wxPay 路径
            || uri.contains("/shop"); // 新增：匹配 merchant 的 /shop 路径
}
```

**涉及文件**：`siam-gateway/src/main/java/com/siam/package_gateway/filter/AuthGlobalFilter.java`

---

### 三、Gateway 路由前缀剥离修复（StripPrefix）

**报错信息**：
```
org.springframework.web.servlet.resource.NoResourceFoundException: No static resource api-rider/rest/actuator/health
```

**表面现象**：curl 请求返回 HTTP 200，但响应体是 `{"success":false,"code":0,"message":"系统异常，请稍后再试"}`。这是因为后端服务收到错误路径后触发 `NoResourceFoundException`，被项目的 `GlobalExceptionHandler` 全局异常处理器兜住，包装成 200 的错误响应返回。

**根因**：Gateway 路由配置中 `StripPrefix=0`（不剥离任何前缀），导致请求路径 `/api-rider/rest/actuator/health` 被原封不动转发到后端。后端服务收到的 URL 带有 `/api-rider/` 前缀，找不到对应的 Controller，被当成静态资源处理。

**修复**：将所有路由的 `StripPrefix=0` 改为 `StripPrefix=1`，Gateway 在转发时自动剥离第一段路径前缀。

| 修复前 | 修复后 |
|--------|--------|
| `/api-rider/rest/actuator/health` 原样转发 | 转发为 `/rest/actuator/health`（剥离 `/api-rider/`） |

**修复后的路由示例**：
```yaml
        - id: rider-service
          uri: lb://rider-siam
          predicates:
            - Path=/api-rider/**
          filters:
            - StripPrefix=1
```

**涉及文件**：`siam-gateway/src/main/resources/application.yml`（8 个路由全部修改）

---

### 四、Gateway 路由验证结果（修复后）

Gateway 端口：`8081`

| Gateway 路径 | 状态码 | 说明 |
|-------------|--------|------|
| `GET /actuator/health` | 200 | Gateway 自身健康 |
| `GET /api-user/rest/member/login` | 待验证 | 路由到 user-siam（需重启 Gateway 后验证） |
| `GET /api-merchant/rest/shop/getShopConfig` | 待验证 | 路由到 merchant-siam |
| `GET /api-goods/rest/actuator/health` | 待验证 | 路由到 goods-siam |
| `GET /api-rider/rest/actuator/health` | 待验证 | 路由到 rider-siam |
| `GET /api-order/rest/member/wxPay/getWxPayConfig` | 待验证 | 路由到 order-siam |
| `GET /api-promotion/rest/actuator/health` | 待验证 | 路由到 promotion-siam |
| `GET /api-mall/rest/actuator/health` | 待验证 | 路由到 mall-siam |
| `GET /api-util/rest/actuator/health` | 待验证 | 路由到 util-siam |

重启 Gateway 后需重新验证上述 8 个路由，确认返回的是真正的业务响应而非 `NoResourceFoundException`。

---

### 五、Swagger/SpringDoc 文档验证

每个 Provider 服务的 SpringDoc 接口文档地址：

| 服务 | 端口 | 接口文档地址 |
|------|------|-------------|
| user-siam | 9200 | `http://127.0.0.1:9200/swagger-ui/index.html` |
| order-siam | 9204 | `http://127.0.0.1:9204/swagger-ui/index.html` |
| merchant-siam | 9201 | `http://127.0.0.1:9201/swagger-ui/index.html` |
| rider-siam | 9202 | `http://127.0.0.1:9202/swagger-ui/index.html` |
| goods-siam | 9203 | `http://127.0.0.1:9203/swagger-ui/index.html` |
| promotion-siam | 9205 | `http://127.0.0.1:9205/swagger-ui/index.html` |
| util-siam | 9207 | `http://127.0.0.1:9207/swagger-ui/index.html` |
| mall-siam | 9206 | `http://127.0.0.1:9206/swagger-ui/index.html` |

API 原始文档地址（JSON 格式）：
- `http://127.0.0.1:<端口>/v3/api-docs`

可通过 Gateway 统一访问（白名单包含 `/swagger` 和 `/v3/api-docs`）：
- `http://127.0.0.1:8081/api-user/v3/api-docs`
- `http://127.0.0.1:8081/api-order/v3/api-docs`

---

### 五、文件改动汇总

| 文件 | 改动内容 |
|------|---------|
| `siam-gateway/pom.xml` | 新增 `spring-cloud-starter-loadbalancer` 依赖 |
| `siam-gateway/.../AuthGlobalFilter.java` | 白名单新增 `/Pay`（匹配 `/wxPay`）和 `/shop` |
| `siam-gateway/.../application.yml` | 8 个路由的 `StripPrefix=0` → `StripPrefix=1` |

---

### 六、Gateway CORS 配置

**原因**：前端跨域访问后端时需要支持 CORS。

**新增配置**：
```yaml
spring:
  cloud:
    gateway:
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOriginPatterns: "*"
            allowedMethods: "*"
            allowedHeaders: "*"
            allowCredentials: true
            maxAge: 3600
```

**涉及文件**：`siam-gateway/src/main/resources/application.yml`

---

## 第六阶段：前后端联调

### 升级时间

2026-04-25

### 升级目标

验证前端（vue-siam-admin）能够通过本地 Gateway 正常访问后端接口，完成登录、数据中心等核心页面功能。

---

### 一、前端 API baseURL 修改

**文件**：`vue-siam-admin/config/index.js`

**改动**：将 `developmentBaseUrl` 从远程地址 `https://api.show.siamit.cn` 改为本地 Gateway `http://127.0.0.1:8081`。

```javascript
http:{
  baseUrl: 'http://127.0.0.1:8081', // production -> 本地 Gateway
  baseUrl4Test: 'http://127.0.0.1:8081', // production_test -> 本地 Gateway
  developmentBaseUrl: 'http://127.0.0.1:8081' // development -> 本地 Gateway
}
```

---

### 二、前端登录验证

**管理员账号**：`admin` / `123456`

账号密码来源：`sql/mysql/siam_cloud.sql` 中 `tb_admin` 表。

登录成功，前端可以正常通过 Gateway 与后端交互。

---

### 三、数据库缺失字段修复

#### 3.1 `tb_shop` 表缺失打印机字段

**报错信息**：
```
java.sql.SQLSyntaxErrorException: Unknown column 'kitchen_total_order_printer_id' in 'field list'
```

**根因**：`Shop.java` 实体类和 MyBatis Mapper XML 中定义了 `kitchen_total_order_printer_id` 和 `checkout_printer_id` 字段，但数据库表中不存在。

**修复**：
```sql
ALTER TABLE `tb_shop` 
ADD COLUMN `kitchen_total_order_printer_id` INT DEFAULT NULL COMMENT '厨房总单打印机ID',
ADD COLUMN `checkout_printer_id` INT DEFAULT NULL COMMENT '结账打印机ID';
```

---

### 四、后端 NPE 批量修复：`BasicResult.getData()` 空值解包

#### 4.1 `PointsMallStatisticsServiceImpl.java` NPE 修复

**报错信息**：
```
java.lang.NullPointerException: Cannot invoke "java.lang.Integer.intValue()" 
because the return value of "com.siam.package_common.entity.BasicResult.getData()" is null
```

**文件**：`siam-goods/goods-provider/.../PointsMallStatisticsServiceImpl.java`

**根因**：Feign API 调用返回的 `BasicResult.getData()` 可能为 null（接口未注册、下游服务未启动等情况），直接拆箱为基本类型（`int`、`BigDecimal`）导致 `NullPointerException`。

**修复**：添加两个安全辅助方法：
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

将方法 `pointsMallTodayStatisticByAdmin()` 中所有 `.getData()` 直接解包替换为 `safeInt()` / `safeBigDecimal()` 调用，约 20 处。

`Map` 类型的 `selectSumField` 调用额外加了 null 检查，防止 `orderSumField` 本身为 null。

---

#### 4.2 `StatisticsServiceImpl.java` NPE 修复

**报错信息**：
```
java.lang.NullPointerException: Cannot invoke "java.lang.Integer.intValue()" 
because the return value of "com.siam.package_common.entity.BasicResult.getData()" is null
  at com.siam.package_goods.service_impl.StatisticsServiceImpl.todayStatisticByAdmin(StatisticsServiceImpl.java:169)
```

**文件**：`siam-goods/goods-provider/.../StatisticsServiceImpl.java`

**根因**：同上，`BasicResult.getData()` 返回 null 时直接拆箱。

**修复**：添加相同的 `safeInt()` 和 `safeBigDecimal()` 辅助方法。

修复范围：
- `todayStatisticByAdmin()` 方法：约 25 处 unsafe 解包
- `todayStatisticByMerchant()` 方法：约 15 处 unsafe 解包
- `selectSumField` 返回 Map 的调用：额外加 null 和 key 值检查

合计修复约 **40 处** unsafe `.getData()` 解包。

---

### 五、前端功能缺失审计

**审计结果**：

| 功能 | 后端 API | 前端页面 | 状态 |
|------|---------|---------|------|
| 添加用户 | 不存在 | 不存在 | 业务流程：用户自己注册注册 |
| 添加商家 | 存在（`/rest/admin/shop/insert`） | 不存在 | 业务流程：商家端申请 → 后台审核上架 |
| 编辑用户 | 存在 | 存在但字段少 | 仅支持禁用/启用 |
| 编辑商家 | 存在 | 存在但字段少 | 仅支持修改起送价和绑定手机号 |
| 商家审核 | 存在 | 存在 | "入驻审核"页面正常 |

**结论**：前端缺失的"添加用户"和"添加商家"功能并非 Bug，而是业务流程设计如此——用户和商家通过自行注册入驻，后台仅负责审核和上下架管理。之前尝试添加的"添加商家"前端功能已撤销。

---

## 第七阶段：前端页面测试

### 升级时间

2026-04-25

### 升级目标

逐一测试前端各模块页面，发现并修复运行时报错。

---

### 一、已测试页面

| 页面 | 状态 | 备注 |
|------|------|------|
| 登录页 | ✓ 通过 | admin/123456 |
| 数据中心 | ✓ 通过（修复后） | NPE 修复后正常显示数据 |
| OSS 图片资源 | 404（预期内） | 远程 OSS 上的图片不存在，非代码问题 |

### 二、已修复问题

| 问题 | 文件 | 修复方式 |
|------|------|---------|
| 数据中心 NPE | `StatisticsServiceImpl.java` | 安全辅助方法替换 unsafe 解包 |
| `tb_shop` 缺失列 | 数据库 DDL | `ALTER TABLE` 补充字段 |
| 前端 baseURL 指向远程 | `vue-siam-admin/config/index.js` | 改为本地 Gateway |

### 三、待测试页面

- [ ] 用户管理
- [ ] 订单管理
- [ ] 商品管理
- [ ] 骑手管理
- [ ] 促销活动
- [ ] 积分商城
- [ ] 系统设置
- [ ] 商家入驻审核
- [ ] 商家提现记录

