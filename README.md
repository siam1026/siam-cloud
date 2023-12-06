<p align=center>
  <a href="http://www.siamit.cn">
    <img src="./doc/images/gitee/logo.png" alt="暹罗外卖" style="width:200px;height:200px">
  </a>
</p>

<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">暹罗外卖 v1.0</h1>
<h4 align="center">基于 Vue/Element UI 和 Spring Cloud & Alibaba 前后端分离的分布式微服务架构</h4>

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


##  前言

**微信公众号【[暹罗siam](https://github.com/siam1026/siam-cloud/raw/master/doc/images/wechat/公众号.jpg)】**，未来将会在公众号上持续性的输出很多原创小知识以及学习资源，欢迎各位小伙伴关注我，和我一起共同学习，同时我也希望各位小伙伴能够给**暹罗外卖**项目多多 **Star** 支持，您的**点赞**就是我维护的动力！

<p align=center>
    <img src="./doc/images/wechat/公众号.jpg" width="500" />
</p>

项目已有较详细的 [项目搭建文档](https://gitee.com/siam1026/siam-cloud/wikis/pages) ，同时包括了 **Windows**、**Linux** 以及 **Docker** 环境下暹罗外卖的搭建。在使用过程中遇到问题时，首先认真阅读**项目搭建文档**~

【提问】推荐使用 [Gitee issue](https://github.com/siam1026/siam-cloud/issues) 进行提问，因为issue解决后能够保留解决记录，帮助其它小伙伴避坑。其次可以使用 <a href="https://github.com/siam1026/siam-cloud#关注和交流">QQ群 </a>  或者 <a href="https://github.com/siam1026/siam-cloud#关注和交流">微信群 </a> 进行提问。群里提问注意提问的时间，把遇到**问题的详细过程都描述清楚**，最好**配上图文信息**，这样能有利于更高效的解决问题。




## 项目介绍

暹罗外卖是一款Java外卖配送系统，适用于多商户入驻。对标美团外卖、饿了么。系统包含用户端、商家端、配送端以及总管理后台，在线下单，商家接单，骑手抢单配送；

1. 商家管理，商家入驻申请，商家审核
2. 商品管理，单规格、多规格商品管理，品牌、分类管理、商品评价、商品组
3. 库存管理，门店要货 、门店调拨、商品入库、商品出库、商品盘点
4. 订单管理，订单支付、发货、取消、售后等
5. 同城配送，配售收费、配送人员
6. 会员管理，会员列表、会员等级、会员提现等
7. 内容管理，官方资讯、活动通知、素材管理、常见问题
8. 营销推广，首页轮播、广告管理、优惠券、邀请好友注册返现
9. 应用设置，基础设置、微信支付、多媒体存储、短信设置
10. 统计分析，商品销量统计、会员增长统计、订单销量统计
11. 积分商城，商品管理、营销管理、物流管理等
12. 订单小票打印，本地打印、云端打印
13. Excel数据导出，导出表格更加美观、可视


## 软件架构

* 前端使用uni-app开发，可打包部署到微信小程序、APP、H5
* Web端使用vue + Element开发
* 服务端使用java语言开发，技术栈：Spring Cloud & Alibaba + Redis + RocketMQ + WebSocket + ElasticSearch + ELK + Sentinel + Seata + SkyWalking + SpringBoot Admin + Promethues + Grafana

## 相关资料获取

1. 获取sql文件
2. 获取接口文档
3. 获取前端代码

[请加入QQ群或微信群获取](https://github.com/siam1026/siam-cloud#关注和交流)，进群前先在右上角Star本项目

## 关联产品
暹罗点餐 - 多门店连锁品牌的餐饮点餐系统，单体服务架构：[https://github.com/siam1026/siam-server](https://github.com/siam1026/siam-server)

## 系统模块

~~~
com.siam     
├── vue-siam-admin        // 前端-调度后台 [80]
├── vue-siam-shop         // 前端-商家后台 [80]
├── wxapplet-siam-user    // 前端-微信小程序
├── siam-eureka           // 注册中心 [9000]
├── siam-zuul             // 网关服务 [8080]
├── siam-user             // 用户服务 [9200]
├── siam-goods            // 商品服务 [9201]
├── siam-order            // 订单服务 [9202]
├── siam-monitor          // 监控服务 [9100]
├── siam-common           // 通用模块
├── siam-feign            // feign接口模块
├── siam-parent           // 父工程模块
├── siam-weixin           // 微信模块
~~~


## 架构图

![image text](./doc/images/gitee/server.png)


## 内置功能

1.  门店展示：小程序首页展示附近的门店信息。
2.  外卖下单：小程序上进行外卖下单、餐饮点餐等。
3.  会员中心：小程序上可开通会员，享受增值服务。
4.  邀请返现：小程序上邀请好友可以得到返现奖励。
5.  积分商城：可以使用积分在商城进行购买。
6.  订单小票打印：商家后台可以连接本地打印机、Wifi打印机来打印订单小票。
7.  骑手配送：对接第三方达达配送、商家自建骑手配送。
8.  商家入驻：支持商家主动填写门店信息，调度后台进行审核。
9.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
10.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
11.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
12.  参数管理：对系统动态配置常用参数。
13.  通知公告：系统通知公告信息发布维护。
14.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
15. 登录日志：系统登录日志记录查询包含登录异常。
16. 在线用户：当前系统中活跃用户状态监控。
17. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
18. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
19. 系统接口：根据业务代码自动生成相关的api接口文档。
20. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
21. 在线构建器：拖动表单元素生成相应的HTML代码。
22. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。

## 站点演示

> 【演示商家后台】：https://spa.show.siamit.cn/shop
>
> 【演示调度后台】：https://spa.show.siamit.cn/admin
>
> 【演示账号】：[点击获取](https://github.com/siam1026/siam-cloud/raw/master/doc/images/wechat/公众号_演示账号.png)
>
> 【小程序】暹罗外卖的移动端版本，可以扫码体验。

|                                                          |
| :------------------------------------------------------: |
| <img src="./doc/images/wechat/微信小程序.jpg" width="200" /> |

## 项目中初始用户和密码

- **小程序登录**：
账号密码登录：siam，123456
手机验证码登录：13121865386，123456
- **商家后台登录**：用户：admin-ludian，密码：123456
- **调度中心登录**：用户：admin，密码：123456

## 项目文档

文档地址：https://gitee.com/siam1026/siam-cloud/wikis/pages

## 项目地址

目前项目托管在 **Gitee** 和 **Github** 平台上中，欢迎大家 **Star** 和 **Fork** 支持~

- Gitee地址：https://gitee.com/siam1026/siam-cloud
- Github地址：https://github.com/siam1026/siam-cloud


## 关注和交流

为了方便小伙伴们沟通交流，我创建了**微信群**（备注：**加群**），目前项目还存在很多不足之处，欢迎各位老哥进群进行技术交流，为了防止广告进入，希望加群的时候能添加备注，谢谢~

|                   微信群【备注：加群】                   | QQ群（备注：`暹罗外卖`）                       |
| :------------------------------------------------------: | :------------------------------------------------------: |
| <img src="https://github.com/siam1026/siam-cloud/raw/master/doc/images/wechat/添加暹罗.jpg" width="200" /> | <img src="https://github.com/siam1026/siam-cloud/raw/master/doc/images/qq/qqGroup2.jpg" width="200" /> |


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


## 赞赏

**服务器**和**域名**等服务的购买和续费都会**产生一定的费用**，为了**维持项目的正常运作**，如果觉得本项目**对您有帮助**的话，欢迎朋友能够**给予一些支持**，暹罗将用于**提升服务器配置**，感谢小伙伴们的支持（ **ps**: 小伙伴赞赏的时候可以备注一下下~）

|                       微信                       |                      支付宝                       |
| :----------------------------------------------: | :-----------------------------------------------: |
| <img src="./doc/images/wechat/wx_payment.png" width="200" /> | <img src="./doc/images/wechat/zfb_payment.png" width="200" /> |

