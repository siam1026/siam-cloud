# 暹罗外卖

<p align=center>
  <a href="http://www.siamit.cn">
    <img src="./doc/images/gitee/logo.png" alt="暹罗外卖" style="width:200px;height:200px">
  </a>
</p>
<p align=center>
   暹罗外卖，一个基于微服务架构的前后端分离外卖(餐饮点餐)系统
</p>
<p align="center">
<a target="_blank" href="https://github.com/siam1026/siam-cloud">
    	<img src="https://img.shields.io/hexpm/l/plug.svg" ></img>
		<img src="https://img.shields.io/badge/JDK-1.8+-green.svg" ></img>
        <img src="https://img.shields.io/badge/nodejs-14.x-green" ></img>
        <img src="https://img.shields.io/badge/springboot-2.2.2.RELEASE-green" ></img>
        <img src="https://img.shields.io/badge/SpringCloud-Hoxton.RELEASE-brightgreen" ></img>
        <img src="https://img.shields.io/badge/vue-2.5.17-green" ></img>
        <img src="https://img.shields.io/badge/swagger-3.0.0-brightgreen" ></img>
        <img src="https://img.shields.io/badge/mybatis--plus-3.1.2-green" ></img>
</a></p>



[项目介绍](#项目介绍) | [站点演示](#站点演示) | [项目特点](#项目特点) | [技术架构](#技术选型) | [项目目录](#项目目录) | [项目文档](#项目文档)  | [快速开始](#快速开始) | [项目截图](#微信小程序截图) | [更新记录](https://github.com/siam1026/siam-cloud/releases)

##  前言

[**暹罗**](https://github.com/siam1026/siam-cloud/raw/master/doc/images/wechat/添加暹罗_加群.jpg)  **微信公众号【[暹罗siam](https://github.com/siam1026/siam-cloud/raw/master/doc/images/wechat/公众号.jpg)】**，未来将会在公众号上持续性的输出很多原创小知识以及学习资源，欢迎各位小伙伴关注我，和我一起共同学习，同时我也希望各位小伙伴能够给**暹罗外卖**项目多多 **Star** 支持，您的**点赞**就是我维护的动力！

<p align=center>
    <img src="./doc/images/wechat/公众号.jpg" width="500" />
</p>

项目已有较详细的  [系统功能清单](https://docs.qq.com/sheet/DYWxmUkpPdkNha0pa?tab=BB08J2) 和 [项目搭建文档](http://siam1026.gitee.io/siam_blog_doc) ，同时包括了 **Windows**、**Linux** 以及 **Docker** 环境下暹罗外卖的搭建。在使用过程中遇到问题时，首先认真阅读**项目搭建文档** ，同时 [FAQ文档](待实现) 还收集了小伙伴咨询的问题，可以提前阅读~

【提问前】可以先 [百度](https://www.baidu.com/) 或者 [Google](https://www.google.com/) 进行解决，有的问题通过**搜索引擎**很快就能得到解决

【提问前】可以首先看看 [issue](https://github.com/siam1026/siam-cloud/issues) 或者  [FAQ文档](待实现)，可能你的问题别人也遇到过

【提问前】可以提前阅读 [如何向开源社区提问题](待实现)

【提问】推荐使用 [Gitee issue](https://github.com/siam1026/siam-cloud/issues) 进行提问，因为issue解决后能够保留解决记录，帮助其它小伙伴避坑。其次可以使用 <a href="##关注&交流">QQ群 </a>  或者 <a href="##前言">微信群 </a> 进行提问。群里提问注意提问的时间，把遇到**问题的详细过程都描述清楚**，最好**配上图文信息**，这样能有利于更高效的解决问题。

## 项目介绍

暹罗外卖( **SiamCloud** )，一个**基于微服务架构的前后端分离外卖(餐饮点餐)系统**。**Web** 端使用 **Vue** + **ElementUi** , 移动端使用 **uniapp** 和 **ColorUI**。后端使用 **SpringCloud** + **SpringBoot** + **Mybatis-plus**进行开发，使用 **Jwt** + **SpringSecurity** 做登录验证和权限校验，使用 **ElasticSearch** 和 **Solr** 作为全文检索服务，使用 **Github Actions**完成外卖(餐饮点餐)的持续集成，使用 **ElasticStack** 收集外卖(餐饮点餐)日志，文件支持**上传本地**、**OSS** 和 **Minio**.

- 暹罗外卖很多地方可能考虑不周，故有能改正的地方，还请各位老哥能够指出~
- 本外卖(餐饮点餐)也是一个非常好的 **SpringBoot**、**SpringCloud**以及 **Vue** 技术的入门学习项目。
- 原来做过 **Vue** + **ElementUi** 做过管理系统，所以现在打算做一套自己的、基于当前最新技术栈、前后端分离的微服务外卖(餐饮点餐)系统。

## 运行配置

暹罗外卖使用了一些监控的 **SpringCloud** 组件，但是并不一定都需要部署，必须启动的服务包含

`nacos`，`nginx`，`rocketmq`， `redis`，`mysql`，`siam-gateway`

其它的服务都可以不启动，也不影正常使用，可以根据自身服务器配置来启动

最低配置：1核4G 【容易宕机】

推荐配置：2核16G 【博主目前配置】

## 站点演示

> 【演示商家后台】：https://spa.show.siamit.cn/shop
>
> 【演示调度后台】：https://spa.show.siamit.cn/admin
>
> 【演示账号】：[点击获取](https://github.com/siam1026/siam-cloud/raw/master/doc/images/wechat/公众号_演示账号.png)
>
> 【小程序】暹罗外卖的移动端版本，可以扫码体验。参考 [暹罗外卖小程序部署](http://siam1026.gitee.io/siam_blog_doc) 文档

|                                                          |
| :------------------------------------------------------: |
| <img src="./doc/images/wechat/微信小程序.jpg" width="200" /> |

## 后期维护

无

## 暹罗外卖社区版

暹罗外卖社区版定位是外卖(餐饮点餐)系统。社区版在原有开源版的基础上加入了更多的功能，如：VIP会员、积分商城、邀请好友返现、商品收藏、小票打印、配送管理(骑手)、支付模块、消息通知、SEO优化、图片敏感审核、消息触达等。

关于暹罗外卖社区功能模块的介绍，可查看：[暹罗外卖社区功能模块介绍](http://siam1026.gitee.io/siam_blog_doc)

关于暹罗外卖社区版的**学习**/**商用授权**，以及查看版本对比，可以查看：[点我跳转](https://www.siamit.cn/version)

社区版演示环境如下，

> 【演示商家后台】：https://spa.show.siamit.cn/shop
>
> 【演示调度后台】：https://spa.show.siamit.cn/admin
>
> 【演示账号】：[点击获取](https://github.com/siam1026/siam-cloud/raw/master/doc/images/wechat/公众号_演示账号.png)
>
> 【小程序】暹罗外卖的移动端版本，可以扫码体验。参考 [暹罗外卖小程序部署](http://siam1026.gitee.io/siam_blog_doc) 文档

|                                                          |
| :------------------------------------------------------: |
| <img src="./doc/images/wechat/微信小程序.jpg" width="200" /> |

目前，暹罗外卖社区版源码暂未开源，可通过 [赞助暹罗](https://www.siamit.cn/version) 的方式获取源码授权，详情可添加暹罗微信了解：[kelubo210](https://github.com/siam1026/siam-cloud/raw/master/doc/images/wechat/添加暹罗.jpg) (备注：暹罗外卖社区)

## 项目中初始用户和密码

- **小程序登录**：
账号密码登录：siam，123456
手机验证码登录：13121865386，123456
- **商家后台登录**：用户：admin-ludian，密码：123456
- **调度中心登录**：用户：admin，密码：123456

## 项目特点

- 友好的代码结构及注释，便于阅读及二次开发
- 实现前后端分离，通过 **Json** 进行数据交互，前端再也不用关注后端技术
- 页面交互使用 **Vue2.x**，极大的提高了开发效率。
- 引入**Swagger** 文档支持，方便编写 **API** 接口文档。
- 引入**RocketMQ** 消息队列，用于邮件发送、更新 **Redis** 和 **Solr**
- 引入**ElasticSearch** 和 **Solr** 作为全文检索服务，并支持可插拔配置
- 引入OSS对象存储，同时支持本地文件存储
- 引入 **SkyWalking** 链路追踪，聚合各业务系统调用延迟数据，可以一眼看出延迟高的服务
- 采用**自定义参数校验注解**，轻松实现后端参数校验
- 采用 **AOP** + 自定义注解 + **Redis** 实现限制IP接口访问次数
- 采用自研的评论模块，实现评论邮件通知
- 采用 **Nacos** 作为服务发现和配置中心，轻松完成项目的配置的维护
- 采用 **Sentinel** 流量控制框架，通过配置再也不怕网站被爆破
- 采用[原生微信小程序]完成暹罗外卖的微信小程序搭建
- 采用 **ElasticStack**【**ElasticSearch** + **Beats** + **Kibana** + **Logstash**】[搭建暹罗外卖日志收集](http://siamit.cn/info/436)
- 采用 **Docker Compose** 完成容器编排，**Portainer** 实现容器可视化，支持[一键部署线上环境](http://www.siamit.cn/info/565)

## 项目文档

文档地址：http://siam159753.gitee.io/siam_blog_doc

## 项目地址

目前项目托管在 **Gitee** 和 **Github** 平台上中，欢迎大家 **Star** 和 **Fork** 支持~

- Gitee地址：https://github.com/siam1026/siam-cloud
- Github地址：https://github.com/siam1026/siam-cloud

## 项目目录

- SiamCloud 是一款基于最新技术开发的多品牌门店的外卖(餐饮点餐)系统。
- siam-eureka： 服务发现和注册【注: Nacos分支没有该目录，用Nacos作为服务发现组件】
- siam-monitor：监控服务，集成SpringBootAdmin用于管理和监控SpringBoot应用程序
- siam-zuul：网关服务【注: Nacos分支没有该目录，用SpringCloud Gateway作为网关】
- siam-common：公共模块，主要用于存放公共的工具类、config配置
- siam-feign：feign接口模块，主要用于存放所有的feign接口
- siam-user：用户服务
- siam-goods：商品服务
- siam-order：订单服务
- siam-parent：父工程
- doc: 是暹罗外卖的一些文档和数据库文件
- vue-siam-admin：VUE的调度后台管理页面
- vue-siam-shop：VUE的商家后台管理页面
- wxapplet-siam-user：基于原生微信小程序的暹罗外卖微信小程序页面

## 技术选型

### 系统架构图

![image text](./doc/images/gitee/server.png)

>  暹罗外卖系统架构图，使用 [Processon](https://www.processon.com/i/5e380df1e4b05b335ffa81e9) 在线绘制

### 后端技术

|      技术      |           说明            |                             官网                             |
| :------------: | :-----------------------: | :----------------------------------------------------------: |
|   SpringBoot   |          MVC框架          | [ https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
|  SpringCloud   |        微服务框架         |           https://spring.io/projects/spring-cloud/           |
| SpringSecurity |      认证和授权框架       |          https://spring.io/projects/spring-security          |
|  MyBatis-Plus  |          ORM框架          |                   https://mp.baomidou.com/                   |
|   Swagger-UI   |       文档生产工具        | [ https://github.com/swagger-api/swagger-ui](https://github.com/swagger-api/swagger-ui) |
|     Kibana     |     分析和可视化平台      |               https://www.elastic.co/cn/kibana               |
| Elasticsearch  |         搜索引擎          | [ https://github.com/elastic/elasticsearch](https://github.com/elastic/elasticsearch) |
|     Beats      |     轻量型数据采集器      |               https://www.elastic.co/cn/beats/               |
|    Logstash    | 用于接收Beats的数据并处理 |              https://www.elastic.co/cn/logstash              |
|      Solr      |         搜索引擎          |                http://lucene.apache.org/solr/                |
|    RocketMQ    |         消息队列          |   [ https://www.rabbitmq.com/](https://www.rabbitmq.com/)    |
|     Redis      |        分布式缓存         |                      https://redis.io/                       |
|     Docker     |        容器化部署         |      [ https://www.docker.com](https://www.docker.com/)      |
|     Druid      |       数据库连接池        | [ https://github.com/alibaba/druid](https://github.com/alibaba/druid) |
|     OSS     |     OSS - 对象储存     |         https://developer.qiniu.com/sdk#official-sdk         |
|      JWT       |        JWT登录支持        |                 https://github.com/jwtk/jjwt                 |
|     SLF4J      |         日志框架          |                    http://www.slf4j.org/                     |
|     Lombok     |     简化对象封装工具      | [ https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok) |
|     Nginx      |  HTTP和反向代理web服务器  |                      http://nginx.org/                       |
|    JustAuth    |     第三方登录的工具      |             https://github.com/justauth/JustAuth             |
|     Hutool     |      Java工具包类库       |                  https://hutool.cn/docs/#/                   |
|    阿里大于    |       短信发送平台        |            https://doc.alidayu.com/doc2/index.htm            |
| Github Actions |        自动化部署         |              https://help.github.com/en/actions              |
|     SkyWalking     |         链路追踪          |             https://github.com/openzipkin/zipkin             |
| Flexmark-java  |     Markdown转换Html      |            https://github.com/vsch/flexmark-java             |
|   Ip2region    |     离线IP地址定位库      |          https://github.com/lionsoul2014/ip2region           |
|     Minio      |     本地对象存储服务      |                       https://min.io/                        |
| Docker Compose |      Docker容器编排       |               https://docs.docker.com/compose/               |
|   Portainer    |     Docker可视化管理      |            https://github.com/portainer/portainer            |

### 前端技术

|         技术          |                  说明                   |                             官网                             |
| :-------------------: | :-------------------------------------: | :----------------------------------------------------------: |
|        Vue.js         |                前端框架                 |                      https://vuejs.org/                      |
|      Vue-router       |                路由框架                 |                  https://router.vuejs.org/                   |
|         Vuex          |            全局状态管理框架             |                   https://vuex.vuejs.org/                    |
|        Nuxt.js        |        创建服务端渲染 (SSR) 应用        |                    https://zh.nuxtjs.org/                    |
|        Element        |               前端ui框架                |    [ https://element.eleme.io](https://element.eleme.io/)    |
|         Axios         |              前端HTTP框架               | [ https://github.com/axios/axios](https://github.com/axios/axios) |
|        Echarts        |                图表框架                 |                      www.echartsjs.com                       |
|       CKEditor        |              富文本编辑器               |                    https://ckeditor.com/                     |
|     Highlight.js      |            代码语法高亮插件             |         https://github.com/highlightjs/highlight.js          |
|        Vditor         |             Markdown编辑器              |             https://github.com/Vanessa219/vditor             |
|      vue-cropper      |              图片裁剪组件               |           https://github.com/xyxiao001/vue-cropper           |
| vue-image-crop-upload |           vue图片剪裁上传组件           |      https://github.com/dai-siki/vue-image-crop-upload       |
|   vue-emoji-comment   |          Vue Emoji表情评论组件          |       https://github.com/pppercyWang/vue-emoji-comment       |
|     clipboard.js      |            现代化的拷贝文字             |                  http://www.clipboardjs.cn/                  |
|      js-beautify      |           美化JavaScript代码            |         https://github.com/beautify-web/js-beautify          |
|     FileSaver.js      |            保存文件在客户端             |           https://github.com/eligrey/FileSaver.js            |
|      SortableJS       |       功能强大的JavaScript 拖拽库       |                  http://www.sortablejs.com/                  |
|   vue-side-catalog    |               目录导航栏                |        https://github.com/yaowei9363/vue-side-catalog        |
|        uniapp         |            移动端跨平台语言             |                  https://uniapp.dcloud.io/                   |
|        colorUi        |         专注视觉的小程序组件库          |             https://github.com/weilanwl/ColorUI              |
|       showdown        | 用Javascript编写的Markdown 到Html转换器 |            https://github.com/showdownjs/showdown            |
|       turndown        | 用JavaScript编写的HTML到Markdown转换器  |           https://github.com/domchristie/turndown            |

## 快速开始
待实现

## 环境搭建

### 开发工具

|     工具     |       说明        |                             官网                             |
| :----------: | :---------------: | :----------------------------------------------------------: |
|     IDEA     |    Java开发IDE    |           https://www.jetbrains.com/idea/download            |
| RedisDesktop |  Redis可视化工具  | [ https://redisdesktop.com/download](https://redisdesktop.com/download) |
|   WebStorm   |    前端开发IDE    |             https://www.jetbrains.com/webstorm/              |
| SwitchHosts  |   本地Host管理    |             https://oldj.github.io/SwitchHosts/              |
|   X-shell    | Linux远程连接工具 |               https://xshell.en.softonic.com/                |
|    X-ftp     | Linux文件传输工具 |         https://www.netsarang.com/zh/all-downloads/          |
|    SQLyog    |  数据库连接工具   |               https://sqlyog.en.softonic.com/                |
| ScreenToGif  |    Gif录制工具    | [ https://www.screentogif.com/](https://www.screentogif.com/) |

### 开发环境

|     工具      | 版本号 |                             下载                             |
| :-----------: | :----: | :----------------------------------------------------------: |
|      JDK      |  1.8   | https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html |
|     Maven     | 3.3.0+ |                   http://maven.apache.org/                   |
| Elasticsearch | 6.3.0  |               https://www.elastic.co/downloads               |
|     Solr      |  7.0   |                http://lucene.apache.org/solr/                |
|     MySQL     |  5.6   |                    https://www.mysql.com/                    |
|    Erlang     |  20.3  |                   https://www.erlang.org/                    |
|   RocketMQ    | 3.7.4  |            http://www.rabbitmq.com/download.html             |
|     Nginx     |  1.10  |              http://nginx.org/en/download.html               |
|     Redis     | 3.3.0  |                  https://redis.io/download                   |
|    SkyWalking     | 2.12.5 | https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec |
|     Nacos     | 1.3.2  |          https://github.com/alibaba/nacos/releases           |
|   Sentinel    | 1.7.2  |         https://github.com/alibaba/Sentinel/releases         |

## 致谢

**暹罗外卖**起初参考了很多**开源项目**的**解决方案**，**开源不易，感谢分享**

- 感谢 **[jetbrains]** 提供的开源 **License** 

## 关注&交流

为了方便小伙伴们沟通交流，我创建了**微信群**（备注：**加群**），目前项目还存在很多不足之处，欢迎各位老哥进群进行技术交流，为了防止广告进入，希望加群的时候能添加备注，谢谢~

|                   微信群【备注：加群】                   |
| :------------------------------------------------------: |
| <img src="https://github.com/siam1026/siam-cloud/raw/master/doc/images/wechat/添加暹罗.jpg" width="200" /> |

## 未来计划

- [ ] 集成Github Actions，完成暹罗外卖持续集成服务
- [ ] 支持第三方登录
- [ ] 按钮级别的细粒度权限控制
- [ ] 增加评论表情
- [ ] 增加数据字典管理
- [ ] 增加一个FAQ常见问题文档
- [ ] 新建Nacos分支，用于替换Eureka作为服务发现组件和配置中心
- [ ] 使用Minio [搭建对象存储服务](http://www.siamit.cn/#/info?blogUid=a1058b2d030310e2c5d7b0584e514f1f)
- [ ] 使用DockerCompose完成 [外卖(餐饮点餐)一键部署](http://www.siamit.cn/info/565)
- [ ] 使用Portainer对Docker镜像可视化管理
- [ ] 完善爬虫模块
- [ ] 增加K8S部署暹罗外卖教程
- [ ] 增加大屏数据展示页面
- [ ] 增加定时任务模块
- [ ] 使用Freemark页面静态化技术对外卖(餐饮点餐)详情页静态化
- [ ] 使外卖(餐饮点餐)能被搜索引擎收录

## 贡献代码

开源项目离不开大家的支持，如果您有好的想法，遇到一些 **BUG** 并修复了，以及 [暹罗外卖文档](http://siam1026.gitee.io/siam_blog_doc) 上有错误的地方订正过来了，欢迎小伙伴们提交 **Pull Request** 参与开源贡献

1. **fork** 本项目到自己的 **repo**
2. 把 **fork** 过去的项目也就是你仓库中的项目 **clone** 到你的本地
3. 修改代码
4. **commit** 后 **push** 到自己的库
5. 发起**PR**（ **pull request**） 请求，提交到  **master** 分支
6. 等待作者合并

## 开源协议

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

## 赞赏

**服务器**和**域名**等服务的购买和续费都会**产生一定的费用**，为了**维持项目的正常运作**，如果觉得本项目**对您有帮助**的话，欢迎朋友能够**给予一些支持**，暹罗将用于**提升服务器配置**，感谢小伙伴们的支持（ **ps**: 小伙伴赞赏的时候可以备注一下下~）

|                       微信                       |                      支付宝                       |
| :----------------------------------------------: | :-----------------------------------------------: |
| <img src="./doc/images/wechat/wx_payment.png" width="200" /> | <img src="./doc/images/wechat/zfb_payment.png" width="200" /> |

## 微信小程序截图

|                        微信小程序                         |                                                       |
| :----------------------------------------------------: | :---------------------------------------------------: |
|      ![image text](./doc/images/wxapplet-user/home.jpg)       |    ![image text](./doc/images/wxapplet-user/shop.jpg)    |
|      ![image text](./doc/images/wxapplet-user/order_takeout.jpg)       |    ![image text](./doc/images/wxapplet-user/order_list.jpg)    |
|      ![image text](./doc/images/wxapplet-user/coupons.jpg)       |    ![image text](./doc/images/wxapplet-user/points_mall_home.jpg)    |
|      ![image text](./doc/images/wxapplet-user/my.jpg)       |    ![image text](./doc/images/wxapplet-user/vip.jpg)    |
|      ![image text](./doc/images/wxapplet-user/reward_withdrawal.jpg)       |    ![image text](./doc/images/wxapplet-user/invite.jpg)    |

## 网站截图

|                        商家后台                         |                                                       |
| :----------------------------------------------------: | :---------------------------------------------------: |
|      ![image text](./doc/images/vue-shop/statisticGraph.png)       |    ![image text](./doc/images/vue-shop/refundOrderList.png)    |
|      ![image text](./doc/images/vue-shop/goodsList.png)       |    ![image text](./doc/images/vue-shop/couponsList.png)    |
|      ![image text](./doc/images/vue-shop/shopInfoImportant.png)       |    ![image text](./doc/images/vue-shop/ticketPrinterList.png)    |
|                       **调度后台**                        |                                                       |
|       ![image text](./doc/images/vue-admin/statisticGraph.png)        |      ![image text](./doc/images/vue-admin/memberList.png)       |
|       ![image text](./doc/images/vue-admin/refundOrderList.png)        |      ![image text](./doc/images/vue-admin/shopList.png)       |
|       ![image text](./doc/images/vue-admin/shopListOfApplyChangeData.png)        |      ![image text](./doc/images/vue-admin/couponsList.png)       |
