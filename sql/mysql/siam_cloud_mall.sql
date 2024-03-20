SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_points_mall_coupons
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_coupons`;
CREATE TABLE `tb_points_mall_coupons` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(100) DEFAULT NULL COMMENT '优惠卷名称',
  `preferential_type` int(11) DEFAULT NULL COMMENT '优惠类型，1=折扣，2=满减',
  `discount_amount` decimal(10,2) DEFAULT '1.00' COMMENT '折扣额度',
  `limited_price` decimal(10,2) DEFAULT '0.00' COMMENT '满足价格（元，满足该价格才能使用）',
  `reduced_price` decimal(10,2) DEFAULT '0.00' COMMENT '减价额度(元)',
  `description` varchar(500) DEFAULT NULL COMMENT '使用规则描述',
  `valid_type` int(1) NOT NULL DEFAULT '2' COMMENT '时效:1绝对时效（领取后XXX-XXX时间段有效）  2相对时效（领取后N天有效）',
  `valid_start_time` datetime DEFAULT NULL COMMENT '使用开始时间',
  `valid_end_time` datetime DEFAULT NULL COMMENT '使用结束时间',
  `valid_days` int(3) NOT NULL DEFAULT '0' COMMENT '自领取之日起有效天数',
  `is_delete` tinyint(1) DEFAULT '0' COMMENT '是否被删除，0-否，1-是',
  `source` int(2) DEFAULT NULL COMMENT '优惠券发放来源 1=商家中心 2=调度中心',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_coupons
-- ----------------------------

-- ----------------------------
-- Table structure for tb_points_mall_coupons_goods_relation
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_coupons_goods_relation`;
CREATE TABLE `tb_points_mall_coupons_goods_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `coupons_id` int(11) DEFAULT NULL COMMENT '优惠卷id',
  `goods_id` int(11) DEFAULT NULL COMMENT '商品id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_coupons_goods_relation
-- ----------------------------

-- ----------------------------
-- Table structure for tb_points_mall_coupons_member_relation
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_coupons_member_relation`;
CREATE TABLE `tb_points_mall_coupons_member_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `coupons_id` int(11) DEFAULT NULL COMMENT '优惠卷id',
  `coupons_name` varchar(100) DEFAULT NULL COMMENT '优惠卷名称',
  `member_id` int(11) DEFAULT NULL COMMENT '用户id',
  `start_time` datetime DEFAULT NULL COMMENT '生效时间',
  `end_time` datetime DEFAULT NULL COMMENT '过期时间',
  `is_used` tinyint(1) DEFAULT '0' COMMENT '是否已经使用，0=未使用，1=已使用',
  `is_expired` tinyint(1) DEFAULT '0' COMMENT '是否过期，0=未过期，1=已过期',
  `is_valid` tinyint(1) DEFAULT '1' COMMENT '是否有效，0-否，1-是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_coupons_member_relation
-- ----------------------------

-- ----------------------------
-- Table structure for tb_points_mall_full_reduction_rule
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_full_reduction_rule`;
CREATE TABLE `tb_points_mall_full_reduction_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `shop_id` int(11) DEFAULT NULL COMMENT '店铺id',
  `name` varchar(100) DEFAULT NULL COMMENT '规则名称',
  `status` int(11) DEFAULT NULL COMMENT '活动状态，1=开启，2=关闭',
  `limited_price` decimal(10,2) DEFAULT '0.00' COMMENT '满足价格（元，满足该价格才能使用）',
  `reduced_price` decimal(10,2) DEFAULT '0.00' COMMENT '减价额度(元)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_full_reduction_rule
-- ----------------------------
INSERT INTO `tb_points_mall_full_reduction_rule` VALUES ('2', null, '满199减99', '1', '5000.00', '4999.00', '2021-04-20 23:44:45', null);

-- ----------------------------
-- Table structure for tb_points_mall_goods
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_goods`;
CREATE TABLE `tb_points_mall_goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `shop_id` int(11) DEFAULT NULL COMMENT '店铺id',
  `name` varchar(50) NOT NULL COMMENT '商品名称',
  `main_image` varchar(128) DEFAULT NULL COMMENT '商品主图',
  `sub_images` varchar(1024) DEFAULT NULL COMMENT '商品子图',
  `detail` varchar(1024) DEFAULT NULL COMMENT '商品详情',
  `detail_images` varchar(1024) DEFAULT NULL COMMENT '详情图片',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '一口价',
  `stock` int(11) DEFAULT '0' COMMENT '库存',
  `is_hot` tinyint(1) DEFAULT '0' COMMENT '是否热门',
  `is_new` tinyint(1) DEFAULT '0' COMMENT '是否新品',
  `status` int(2) DEFAULT '1' COMMENT '状态 1=待上架 2=已上架 3=已下架 4=售罄',
  `is_sale` tinyint(1) unsigned DEFAULT '0' COMMENT '是否开启促销 0-否 1-是',
  `sale_price` decimal(10,2) DEFAULT '0.00' COMMENT '折扣价',
  `monthly_sales` int(11) DEFAULT '0' COMMENT '月销量',
  `total_sales` int(11) DEFAULT '0' COMMENT '累计销量',
  `total_comments` int(11) DEFAULT '0' COMMENT '累计评价',
  `preferential_name` varchar(20) DEFAULT NULL COMMENT '优惠名称',
  `packing_charges` decimal(10,2) DEFAULT '0.00' COMMENT '包装费',
  `product_time` decimal(10,2) DEFAULT '0.00' COMMENT '制作时长(分钟)',
  `exchange_points` int(10) DEFAULT NULL COMMENT '兑换商品所需积分数量',
  `sort_number` int(11) DEFAULT NULL COMMENT '排序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_goods
-- ----------------------------
INSERT INTO `tb_points_mall_goods` VALUES ('1', null, '米诗萱雾面丝绒唇釉大理石哑光染唇液不易脱色持久保湿防水口红', 'data/images/admin/1/deerspot_1615385157622.png', 'data/images/admin/1/deerspot_1615385157622.png,data/images/admin/1/deerspot_1616429825944.png', null, 'data/images/admin/1/deerspot_1617105399351.png,data/images/admin/1/deerspot_1617105401513.png,data/images/admin/1/deerspot_1617105409406.png,data/images/admin/1/deerspot_1617105544308.jpg,data/images/admin/1/deerspot_1617121868742.png', '78.00', '12', '0', '0', '2', '0', '0.00', '34', '34', '0', null, '0.00', '0.00', '78', null, '2021-03-10 22:06:10', '2021-04-25 16:14:22');
INSERT INTO `tb_points_mall_goods` VALUES ('6', null, '米诗萱水漾雪肌光感素颜霜隔离滋养润泽保湿亮肤打底神器懒人霜', 'data/images/admin/1/deerspot_1618927816555.png', 'data/images/admin/1/deerspot_1618927816555.png', null, 'data/images/admin/1/deerspot_1618927673227.png,data/images/admin/1/deerspot_1618927711315.png,data/images/admin/1/deerspot_1618927714000.png,data/images/admin/1/deerspot_1618927716731.png,data/images/admin/1/deerspot_1618927721803.png', '98.00', '3', '0', '0', '2', '0', '0.00', '55', '55', '0', null, '0.00', '0.00', '98', null, '2021-04-20 22:11:00', '2021-04-25 16:14:07');
INSERT INTO `tb_points_mall_goods` VALUES ('7', null, '舒缓新肌焕颜乳', 'data/images/admin/1/deerspot_1618928297521.png', 'data/images/admin/1/deerspot_1618928297521.png', null, 'data/images/admin/1/deerspot_1623679552226.jpg,data/images/admin/1/deerspot_1623679556825.jpg,data/images/admin/1/deerspot_1623679560788.jpg,data/images/admin/1/deerspot_1623679565163.jpg,data/images/admin/1/deerspot_1623679570692.jpg,data/images/admin/1/deerspot_1623679575262.jpg', '138.00', '2', '0', '0', '2', '0', '0.00', '9', '9', '0', null, '0.00', '0.00', '138', null, '2021-04-20 22:23:17', '2021-06-14 22:07:08');
INSERT INTO `tb_points_mall_goods` VALUES ('8', null, '积雪草舒缓滋养喷雾', 'data/images/admin/1/deerspot_1623675506772.jpg', 'data/images/admin/1/deerspot_1623675506772.jpg', null, 'data/images/admin/1/deerspot_1623675611465.png,data/images/admin/1/deerspot_1623675617628.jpg,data/images/admin/1/deerspot_1623675619883.jpg,data/images/admin/1/deerspot_1623675637120.jpg,data/images/admin/1/deerspot_1623675639403.jpg,data/images/admin/1/deerspot_1623675641720.jpg', '138.00', '2', '0', '0', '2', '0', '0.00', '5', '5', '0', null, '0.00', '0.00', '138', null, '2021-04-20 22:36:05', '2021-06-14 21:00:45');
INSERT INTO `tb_points_mall_goods` VALUES ('9', null, '焕彩活肤能量水', 'data/images/admin/1/deerspot_1623679829685.jpg', 'data/images/admin/1/deerspot_1623679829685.jpg', null, 'data/images/admin/1/deerspot_1623680041668.jpg,data/images/admin/1/deerspot_1623680045897.jpg,data/images/admin/1/deerspot_1623680048606.jpg,data/images/admin/1/deerspot_1623680051338.jpg,data/images/admin/1/deerspot_1623680053889.jpg,data/images/admin/1/deerspot_1623680056132.jpg', '138.00', '1', '0', '0', '2', '0', '0.00', '3', '3', '0', null, '0.00', '0.00', '138', null, '2021-04-20 22:54:02', '2021-06-14 22:14:17');
INSERT INTO `tb_points_mall_goods` VALUES ('10', null, '米诗萱素颜柔肤洗卸凝露一瓶双用干洗卸妆湿洗深层清洁不油腻', 'data/images/admin/1/deerspot_1618931788410.png', 'data/images/admin/1/deerspot_1618931788410.png', null, 'data/images/admin/1/deerspot_1618932364284.png,data/images/admin/1/deerspot_1618932382794.png,data/images/admin/1/deerspot_1618932385852.png,data/images/admin/1/deerspot_1618932389826.png,data/images/admin/1/deerspot_1618932392795.png', '98.00', '1', '0', '0', '2', '0', '0.00', '1', '1', '0', null, '0.00', '0.00', '98', null, '2021-04-20 23:26:34', '2021-04-20 23:26:34');
INSERT INTO `tb_points_mall_goods` VALUES ('11', null, '米诗萱MISHIXUAN莹润无暇水光保湿气垫cc霜15g正装+送15g替换装', 'data/images/admin/1/deerspot_1619189960396.png', 'data/images/admin/1/deerspot_1619189960396.png', null, 'data/images/admin/1/deerspot_1618933345104.png,data/images/admin/1/deerspot_1618933348224.png,data/images/admin/1/deerspot_1618933352412.png,data/images/admin/1/deerspot_1618933356775.png,data/images/admin/1/deerspot_1618933360241.png,data/images/admin/1/deerspot_1618933364010.png,data/images/admin/1/deerspot_1618933367410.png,data/images/admin/1/deerspot_1618933371415.png', '138.00', '0', '0', '0', '2', '0', '0.00', '0', '0', '0', null, '0.00', '0.00', '138', null, '2021-04-20 23:42:53', '2021-04-23 22:59:22');
INSERT INTO `tb_points_mall_goods` VALUES ('12', null, '米诗萱纳米金滋养修护眼霜抗老抗衰去黑眼圈淡化细纹去眼袋提拉', 'data/images/admin/1/deerspot_1619186493878.png', 'data/images/admin/1/deerspot_1619186493878.png', null, 'data/images/admin/1/deerspot_1619186763035.png,data/images/admin/1/deerspot_1619186766210.png,data/images/admin/1/deerspot_1619186769873.png,data/images/admin/1/deerspot_1619186773259.png,data/images/admin/1/deerspot_1619186776134.png', '168.00', '1', '0', '0', '2', '0', '0.00', '5', '5', '0', null, '0.00', '0.00', '168', null, '2021-04-23 22:06:19', '2021-04-23 22:06:19');
INSERT INTO `tb_points_mall_goods` VALUES ('13', null, '[盒装]米诗萱补水雪肤蚕丝面膜30ml纯天然蚕丝补水嫩肤精华轻薄', 'data/images/admin/1/deerspot_1623675045984.jpg', 'data/images/admin/1/deerspot_1623675045984.jpg', null, 'data/images/admin/1/deerspot_1623675226062.jpg,data/images/admin/1/deerspot_1623675232863.jpg,data/images/admin/1/deerspot_1623675235321.jpg,data/images/admin/1/deerspot_1623675237979.jpg,data/images/admin/1/deerspot_1623675241033.jpg,data/images/admin/1/deerspot_1623675244622.jpg', '75.00', '2', '0', '0', '2', '0', '0.00', '5', '5', '0', null, '0.00', '0.00', '15', null, '2021-04-23 22:17:04', '2021-06-14 20:54:11');
INSERT INTO `tb_points_mall_goods` VALUES ('14', null, '米诗萱冰晶嫩白防晒喷雾防晒霜防紫外线网红变白美白面部隔离粉底', 'data/images/admin/1/deerspot_1619187580858.png', 'data/images/admin/1/deerspot_1619187580858.png', null, 'data/images/admin/1/deerspot_1619188232205.png,data/images/admin/1/deerspot_1619188235217.png,data/images/admin/1/deerspot_1619188238400.png,data/images/admin/1/deerspot_1619188241086.png,data/images/admin/1/deerspot_1619188244961.png,data/images/admin/1/deerspot_1619188248446.png', '98.00', '0', '0', '0', '2', '0', '0.00', '208', '208', '0', null, '0.00', '0.00', '98', null, '2021-04-23 22:30:52', '2021-04-23 22:30:52');
INSERT INTO `tb_points_mall_goods` VALUES ('15', null, '米诗萱卸妆巾女便携湿巾无刺激一次性擦脸专用卸妆棉深层清洁护肤', 'data/images/admin/1/deerspot_1619189367072.png', 'data/images/admin/1/deerspot_1619189367072.png', null, 'data/images/admin/1/deerspot_1619189667779.png,data/images/admin/1/deerspot_1619189671200.png,data/images/admin/1/deerspot_1619189674702.png', '19.00', '1', '0', '0', '2', '0', '0.00', '3', '3', '0', null, '0.00', '0.00', '19', null, '2021-04-23 22:54:37', '2021-04-23 22:54:37');
INSERT INTO `tb_points_mall_goods` VALUES ('16', null, '草本净肤祛痘膏', 'data/images/admin/1/deerspot_1623678574008.jpg', 'data/images/admin/1/deerspot_1623678574008.jpg', null, 'data/images/admin/1/deerspot_1623678600541.jpg,data/images/admin/1/deerspot_1623678604777.jpg,data/images/admin/1/deerspot_1623678607558.jpg,data/images/admin/1/deerspot_1623678610126.jpg,data/images/admin/1/deerspot_1623678612778.jpg,data/images/admin/1/deerspot_1623678615385.jpg', '168.00', '1', '0', '0', '2', '0', '0.00', '2', '2', '0', null, '0.00', '0.00', '168', null, '2021-04-23 23:49:27', '2021-06-14 21:50:17');
INSERT INTO `tb_points_mall_goods` VALUES ('17', null, '氨基酸净颜平衡洁面乳', 'data/images/admin/1/deerspot_1623678753385.jpg', 'data/images/admin/1/deerspot_1623678753385.jpg', null, 'data/images/admin/1/deerspot_1623678732930.jpg,data/images/admin/1/deerspot_1623678736713.jpg,data/images/admin/1/deerspot_1623678739167.jpg,data/images/admin/1/deerspot_1623678741661.jpg,data/images/admin/1/deerspot_1623678743736.jpg,data/images/admin/1/deerspot_1623678746638.jpg', '98.00', '1', '0', '0', '2', '0', '0.00', '1', '1', '0', null, '0.00', '0.00', '98', null, '2021-04-24 00:00:42', '2021-06-14 21:53:23');
INSERT INTO `tb_points_mall_goods` VALUES ('18', null, '米诗萱D-CUP美乳丰韵套快速增大涂抹式按摩正品美胸丰乳产后下垂', 'data/images/admin/1/deerspot_1619337503111.jpg', 'data/images/admin/1/deerspot_1619337503111.jpg', null, 'data/images/admin/1/deerspot_1619337836672.png,data/images/admin/1/deerspot_1619337839514.png,data/images/admin/1/deerspot_1619337842410.png,data/images/admin/1/deerspot_1619337844815.png,data/images/admin/1/deerspot_1619337847296.png', '298.00', '1', '0', '0', '2', '0', '0.00', '1', '1', '0', null, '0.00', '0.00', '298', null, '2021-04-25 16:04:09', '2021-04-25 16:04:09');
INSERT INTO `tb_points_mall_goods` VALUES ('19', null, '米诗萱氨基无硅油洗护套装350ml+350ml无重金属无矿物油无激素', 'data/images/admin/1/deerspot_1619337949079.png', 'data/images/admin/1/deerspot_1619337949079.png', null, 'data/images/admin/1/deerspot_1619338232529.png,data/images/admin/1/deerspot_1619338235451.png,data/images/admin/1/deerspot_1619338238508.png,data/images/admin/1/deerspot_1619338241068.png,data/images/admin/1/deerspot_1619338243589.png,data/images/admin/1/deerspot_1619338247302.png', '78.00', '2', '0', '0', '2', '0', '0.00', '1', '1', '0', null, '0.00', '0.00', '78', null, '2021-04-25 16:10:49', '2021-04-25 16:10:49');
INSERT INTO `tb_points_mall_goods` VALUES ('20', null, '米诗萱眼线笔不易脱妆不花妆', 'data/images/admin/1/deerspot_1623680156948.jpg', 'data/images/admin/1/deerspot_1623680156948.jpg', null, 'data/images/admin/1/deerspot_1619339871027.png,data/images/admin/1/deerspot_1619339873988.png,data/images/admin/1/deerspot_1619339877316.png,data/images/admin/1/deerspot_1619339880986.png,data/images/admin/1/deerspot_1619339883797.png,data/images/admin/1/deerspot_1619339887617.png', '68.00', '1', '0', '0', '2', '0', '0.00', '2', '2', '0', null, '0.00', '0.00', '68', null, '2021-04-25 16:38:09', '2021-06-14 22:15:59');
INSERT INTO `tb_points_mall_goods` VALUES ('21', null, '[盒装]炭净肤焕颜面膜', 'data/images/admin/1/deerspot_1623674885937.jpg', 'data/images/admin/1/deerspot_1623674885937.jpg', null, 'data/images/admin/1/deerspot_1623674795686.png,data/images/admin/1/deerspot_1623674799183.jpg,data/images/admin/1/deerspot_1623674802048.jpg,data/images/admin/1/deerspot_1623674805024.jpg,data/images/admin/1/deerspot_1623674807500.jpg,data/images/admin/1/deerspot_1623674809749.jpg', '78.00', '1', '0', '0', '2', '0', '0.00', '3', '3', '0', null, '0.00', '0.00', '78', null, '2021-06-14 20:46:51', '2021-06-14 20:54:33');
INSERT INTO `tb_points_mall_goods` VALUES ('22', null, '多效修复隔离妆前乳', 'data/images/admin/1/deerspot_1623675824941.jpg', 'data/images/admin/1/deerspot_1623675824941.jpg', null, 'data/images/admin/1/deerspot_1623675867202.jpg,data/images/admin/1/deerspot_1623675870158.jpg,data/images/admin/1/deerspot_1623675873484.jpg,data/images/admin/1/deerspot_1623675876451.jpg,data/images/admin/1/deerspot_1623675879115.jpg,data/images/admin/1/deerspot_1623675881413.jpg', '98.00', '1', '0', '0', '2', '0', '0.00', '1', '1', '0', null, '0.00', '0.00', '98', null, '2021-06-14 21:04:45', '2021-06-14 21:04:45');
INSERT INTO `tb_points_mall_goods` VALUES ('23', null, '烟酰胺亮肤舒润身体乳', 'data/images/admin/1/deerspot_1623677358654.jpg', 'data/images/admin/1/deerspot_1623677358654.jpg', null, 'data/images/admin/1/deerspot_1623676318802.jpg,data/images/admin/1/deerspot_1623676321602.jpg,data/images/admin/1/deerspot_1623676324461.jpg,data/images/admin/1/deerspot_1623676327228.jpg,data/images/admin/1/deerspot_1623676330226.jpg,data/images/admin/1/deerspot_1623676332381.jpg', '78.00', '1', '0', '0', '2', '0', '0.00', '4', '4', '0', null, '0.00', '0.00', '78', null, '2021-06-14 21:12:16', '2021-06-14 21:29:24');
INSERT INTO `tb_points_mall_goods` VALUES ('24', null, '胶原蛋白冻膜', 'data/images/admin/1/deerspot_1623677741442.jpg', 'data/images/admin/1/deerspot_1623677741442.jpg', null, 'data/images/admin/1/deerspot_1623677675677.jpg,data/images/admin/1/deerspot_1623677678378.jpg,data/images/admin/1/deerspot_1623677681211.jpg,data/images/admin/1/deerspot_1623677684022.jpg,data/images/admin/1/deerspot_1623677687036.jpg,data/images/admin/1/deerspot_1623677689508.jpg', '78.00', '1', '0', '0', '2', '0', '0.00', '2', '2', '0', null, '0.00', '0.00', '78', null, '2021-06-14 21:35:13', '2021-06-14 21:35:44');
INSERT INTO `tb_points_mall_goods` VALUES ('25', null, '清洁抑菌皂', 'data/images/admin/1/deerspot_1623677907505.jpg', 'data/images/admin/1/deerspot_1623677907505.jpg', null, 'data/images/admin/1/deerspot_1623677932190.jpg,data/images/admin/1/deerspot_1623677935464.jpg,data/images/admin/1/deerspot_1623677938687.jpg,data/images/admin/1/deerspot_1623677941501.jpg,data/images/admin/1/deerspot_1623677944235.jpg,data/images/admin/1/deerspot_1623677947166.jpg', '58.00', '1', '0', '0', '2', '0', '0.00', '1', '1', '0', null, '0.00', '0.00', '58', null, '2021-06-14 21:39:10', '2021-06-14 21:39:10');
INSERT INTO `tb_points_mall_goods` VALUES ('26', null, '24K黄金烟酰胺精华', 'data/images/admin/1/deerspot_1623678096696.jpg', 'data/images/admin/1/deerspot_1623678096696.jpg', null, 'data/images/admin/1/deerspot_1623678101409.jpg,data/images/admin/1/deerspot_1623678104324.jpg,data/images/admin/1/deerspot_1623678109144.jpg,data/images/admin/1/deerspot_1623678112297.jpg,data/images/admin/1/deerspot_1623678115353.jpg,data/images/admin/1/deerspot_1623678118149.jpg', '138.00', '1', '0', '0', '2', '0', '0.00', '3', '3', '0', null, '0.00', '0.00', '138', null, '2021-06-14 21:42:00', '2021-06-14 21:42:00');
INSERT INTO `tb_points_mall_goods` VALUES ('27', null, '除螨控油祛痘皂', 'data/images/admin/1/deerspot_1623678272174.jpg', 'data/images/admin/1/deerspot_1623678272174.jpg', null, 'data/images/admin/1/deerspot_1623678276796.jpg,data/images/admin/1/deerspot_1623678279690.jpg,data/images/admin/1/deerspot_1623678282435.jpg,data/images/admin/1/deerspot_1623678286221.jpg,data/images/admin/1/deerspot_1623678289293.jpg,data/images/admin/1/deerspot_1623678292096.jpg', '68.00', '1', '0', '0', '2', '0', '0.00', '3', '3', '0', null, '0.00', '0.00', '68', null, '2021-06-14 21:44:53', '2021-06-14 21:44:53');
INSERT INTO `tb_points_mall_goods` VALUES ('28', null, '多肽净透奢养焕颜霜', 'data/images/admin/1/deerspot_1623679119903.jpg', 'data/images/admin/1/deerspot_1623679119903.jpg', null, 'data/images/admin/1/deerspot_1623679137227.jpg,data/images/admin/1/deerspot_1623679140340.jpg,data/images/admin/1/deerspot_1623679143474.jpg,data/images/admin/1/deerspot_1623679146384.jpg,data/images/admin/1/deerspot_1623679149281.jpg,data/images/admin/1/deerspot_1623679152397.jpg', '158.00', '1', '0', '0', '2', '0', '0.00', '5', '5', '0', null, '0.00', '0.00', '158', null, '2021-06-14 21:59:15', '2021-06-14 21:59:15');
INSERT INTO `tb_points_mall_goods` VALUES ('29', null, '[盒装]烟酰胺原液面膜', 'data/images/admin/1/deerspot_1623685569575.jpg', 'data/images/admin/1/deerspot_1623685569575.jpg', null, 'data/images/admin/1/deerspot_1623685592807.jpg,data/images/admin/1/deerspot_1623685595771.jpg,data/images/admin/1/deerspot_1623685598715.jpg,data/images/admin/1/deerspot_1623685601052.jpg,data/images/admin/1/deerspot_1623685603584.jpg,data/images/admin/1/deerspot_1623685605954.jpg', '78.00', '1', '0', '0', '2', '0', '0.00', '5', '5', '0', null, '0.00', '0.00', '78', null, '2021-06-14 23:47:57', '2021-06-14 23:47:57');

-- ----------------------------
-- Table structure for tb_points_mall_goods_specification
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_goods_specification`;
CREATE TABLE `tb_points_mall_goods_specification` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `goods_id` int(11) NOT NULL COMMENT '商品id',
  `name` varchar(10) NOT NULL COMMENT '商品规格名称',
  `sort_number` int(11) DEFAULT NULL COMMENT '排序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_goods_specification
-- ----------------------------
INSERT INTO `tb_points_mall_goods_specification` VALUES ('1', '1', '色号', '1', '2021-03-15 13:49:19', '2021-03-15 13:49:19');
INSERT INTO `tb_points_mall_goods_specification` VALUES ('2', '19', '规格', '1', '2021-04-25 16:11:16', '2021-04-25 16:11:16');
INSERT INTO `tb_points_mall_goods_specification` VALUES ('3', '20', '颜色', '1', '2021-04-25 16:38:50', '2021-04-25 16:38:50');

-- ----------------------------
-- Table structure for tb_points_mall_goods_specification_option
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_goods_specification_option`;
CREATE TABLE `tb_points_mall_goods_specification_option` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `goods_id` int(11) NOT NULL COMMENT '商品id',
  `goods_specification_id` int(11) NOT NULL COMMENT '商品规格id',
  `name` varchar(10) NOT NULL COMMENT '商品规格选项名称',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '单价/加价金额',
  `stock` int(11) DEFAULT '1' COMMENT '库存 1=有货 2=无货',
  `sort_number` int(11) DEFAULT NULL COMMENT '排序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_goods_specification_option
-- ----------------------------
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('1', '1', '1', '#101豆沙粉', '0.00', '1', '1', '2021-03-15 13:49:19', '2021-03-15 13:49:19');
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('2', '1', '1', '#102南瓜色', '0.00', '1', '2', '2021-03-15 13:49:39', '2021-03-15 13:49:39');
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('3', '1', '1', '#103热恋红', '0.00', '1', '3', '2021-03-15 13:50:01', '2021-03-15 13:50:01');
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('4', '1', '1', '#104甜蜜橘', '0.00', '1', '4', '2021-03-15 13:50:21', '2021-03-15 13:50:21');
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('5', '1', '1', '#105中国红', '0.00', '1', '5', '2021-03-15 13:50:44', '2021-03-15 13:50:44');
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('6', '1', '1', '#106姨妈红', '0.00', '1', '6', '2021-03-15 13:50:55', '2021-03-15 13:50:55');
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('7', '1', '1', '#车厘子红', '0.00', '1', '7', '2021-03-15 13:51:24', '2021-03-15 13:51:24');
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('8', '1', '1', '#锦鲤红', '0.00', '1', '8', '2021-03-15 13:51:45', '2021-03-15 13:51:45');
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('9', '1', '1', '#番茄红', '0.00', '1', '9', '2021-03-15 13:52:09', '2021-03-15 13:52:09');
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('10', '1', '1', '#烟熏玫瑰', '0.00', '1', '10', '2021-03-15 13:52:38', '2021-03-15 13:52:38');
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('11', '19', '2', '洗发露', '0.00', '1', '1', '2021-04-25 16:11:16', '2021-04-25 16:11:16');
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('12', '19', '2', '护发素', '0.00', '1', '2', '2021-04-25 16:11:25', '2021-04-25 16:11:25');
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('13', '19', '2', '洗发露+护发素套装', '60.00', '1', '3', '2021-04-25 16:11:54', '2021-04-25 16:11:54');
INSERT INTO `tb_points_mall_goods_specification_option` VALUES ('14', '20', '3', '黑色', '0.00', '1', '1', '2021-04-25 16:38:50', '2021-04-25 16:38:50');

-- ----------------------------
-- Table structure for tb_points_mall_member_goods_collect
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_member_goods_collect`;
CREATE TABLE `tb_points_mall_member_goods_collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) DEFAULT NULL COMMENT '用户id',
  `shop_id` int(11) DEFAULT NULL COMMENT '店铺id',
  `goods_id` int(11) DEFAULT NULL COMMENT '商品id',
  `is_goods_exists` tinyint(1) DEFAULT '1' COMMENT '商品是否有效 0=无效 1=有效',
  `is_buy` tinyint(1) DEFAULT '0' COMMENT '商品是否购买 0=未购买 1=已购买',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_points_mall_member_goods_collect
-- ----------------------------

-- ----------------------------
-- Table structure for tb_points_mall_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_menu`;
CREATE TABLE `tb_points_mall_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `shop_id` int(11) DEFAULT NULL COMMENT '店铺id',
  `name` varchar(50) CHARACTER SET utf8mb4 NOT NULL COMMENT '菜单分类名称',
  `description` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '菜单分类描述',
  `sort_number` int(11) DEFAULT NULL COMMENT '排序号',
  `icon` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT '菜单图标',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_points_mall_menu
-- ----------------------------
INSERT INTO `tb_points_mall_menu` VALUES ('1', null, '美妆专区', '', null, 'data/images/admin/1/deerspot_1614583892190.png', '2021-03-01 13:24:34', '2021-03-01 15:31:35');

-- ----------------------------
-- Table structure for tb_points_mall_menu_goods_relation
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_menu_goods_relation`;
CREATE TABLE `tb_points_mall_menu_goods_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `menu_id` int(11) NOT NULL COMMENT '菜单分类id',
  `goods_id` int(11) NOT NULL COMMENT '商品id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_points_mall_menu_goods_relation
-- ----------------------------
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('1', '1', '1', '2021-03-10 22:06:10', '2021-03-10 22:06:10');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('6', '1', '6', '2021-04-20 22:11:00', '2021-04-20 22:11:00');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('7', '1', '7', '2021-04-20 22:23:17', '2021-04-20 22:23:17');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('8', '1', '8', '2021-04-20 22:36:05', '2021-04-20 22:36:05');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('9', '1', '9', '2021-04-20 22:54:02', '2021-04-20 22:54:02');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('10', '1', '10', '2021-04-20 23:26:34', '2021-04-20 23:26:34');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('11', '1', '11', '2021-04-20 23:42:53', '2021-04-20 23:42:53');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('12', '1', '12', '2021-04-23 22:06:19', '2021-04-23 22:06:19');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('13', '1', '13', '2021-04-23 22:17:04', '2021-04-23 22:17:04');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('14', '1', '14', '2021-04-23 22:30:52', '2021-04-23 22:30:52');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('15', '1', '15', '2021-04-23 22:54:37', '2021-04-23 22:54:37');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('16', '1', '16', '2021-04-23 23:49:27', '2021-04-23 23:49:27');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('17', '1', '17', '2021-04-24 00:00:42', '2021-04-24 00:00:42');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('18', '1', '18', '2021-04-25 16:04:09', '2021-04-25 16:04:09');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('19', '1', '19', '2021-04-25 16:10:49', '2021-04-25 16:10:49');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('20', '1', '20', '2021-04-25 16:38:09', '2021-04-25 16:38:09');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('21', '1', '21', '2021-06-14 20:46:51', '2021-06-14 20:46:51');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('22', '1', '22', '2021-06-14 21:04:45', '2021-06-14 21:04:45');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('23', '1', '23', '2021-06-14 21:12:16', '2021-06-14 21:12:16');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('24', '1', '24', '2021-06-14 21:35:13', '2021-06-14 21:35:13');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('25', '1', '25', '2021-06-14 21:39:10', '2021-06-14 21:39:10');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('26', '1', '26', '2021-06-14 21:42:00', '2021-06-14 21:42:00');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('27', '1', '27', '2021-06-14 21:44:53', '2021-06-14 21:44:53');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('28', '1', '28', '2021-06-14 21:59:15', '2021-06-14 21:59:15');
INSERT INTO `tb_points_mall_menu_goods_relation` VALUES ('29', '1', '29', '2021-06-14 23:47:57', '2021-06-14 23:47:57');

-- ----------------------------
-- Table structure for tb_points_mall_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_order`;
CREATE TABLE `tb_points_mall_order` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号，供客户查询',
  `goods_total_quantity` int(11) DEFAULT '0' COMMENT '商品总数量',
  `goods_total_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品总金额',
  `packing_charges` decimal(10,2) DEFAULT NULL COMMENT '包装费',
  `delivery_fee` decimal(10,2) DEFAULT '0.00' COMMENT '运费/配送费',
  `actual_price` decimal(10,2) DEFAULT '0.00' COMMENT '实付款',
  `shopping_way` int(2) DEFAULT NULL COMMENT '购物方式 1=自取 2=配送',
  `delivery_address_id` int(11) DEFAULT NULL COMMENT '收货地址id',
  `contact_realname` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `contact_province` varchar(20) DEFAULT NULL COMMENT '省份',
  `contact_city` varchar(20) DEFAULT NULL COMMENT '城市',
  `contact_area` varchar(100) DEFAULT NULL COMMENT '区/县',
  `contact_street` varchar(100) DEFAULT NULL COMMENT '详细地址(具体到街道门牌号)',
  `contact_sex` int(1) DEFAULT '0' COMMENT '联系人性别 0=无 1=先生 2=女士',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `description` varchar(2048) DEFAULT NULL COMMENT '订单描述',
  `status` int(2) DEFAULT '1' COMMENT '订单状态 1=未付款 4=待发货(已处理) 5=已发货 6=已完成 7=售后处理中 8=已退款(废弃选项) 9=售后处理完成 10=已取消(未支付)',
  `trade_id` int(11) DEFAULT NULL COMMENT '用户交易id',
  `order_logistics_id` int(11) DEFAULT NULL COMMENT '物流id',
  `is_invoice` tinyint(1) DEFAULT '0' COMMENT '是否开票',
  `invoice_id` int(11) DEFAULT NULL COMMENT '发票id',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0=正常 1=删除',
  `shop_id` int(11) DEFAULT NULL COMMENT '接单门店id',
  `shop_name` varchar(100) DEFAULT NULL COMMENT '接单门店名称',
  `shop_address` varchar(200) DEFAULT NULL COMMENT '门店地址',
  `cancel_reason` int(2) DEFAULT NULL COMMENT '(未支付)订单取消原因 1=您主动取消 2=订单超时未支付 3=系统异常',
  `payment_deadline` datetime DEFAULT NULL COMMENT '支付截止时间(五分钟内未付款的订单会被自动关闭)',
  `is_printed` tinyint(1) DEFAULT '0' COMMENT '是否已打印',
  `queue_no` int(11) DEFAULT NULL COMMENT '取餐号',
  `full_reduction_rule_id` int(11) DEFAULT NULL COMMENT '使用的满减id',
  `full_reduction_rule_description` varchar(100) DEFAULT NULL COMMENT '使用的满减规则描述',
  `coupons_id` int(11) DEFAULT NULL COMMENT '使用的优惠卷id',
  `coupons_description` varchar(100) DEFAULT NULL COMMENT '使用的优惠卷描述',
  `coupons_member_relation_id` int(11) DEFAULT NULL COMMENT '优惠卷用户关系id',
  `is_change_to_delivery` tinyint(1) DEFAULT '0' COMMENT '是否由自取订单改为配送 0=否 1=是',
  `change_to_delivery_out_trade_no` varchar(50) DEFAULT NULL COMMENT '自取订单改为配送的商户单号',
  `change_to_delivery_trade_id` int(11) DEFAULT NULL COMMENT '自取订单改为配送的用户交易id',
  `platform_extract_ratio` decimal(10,2) DEFAULT '0.00' COMMENT '平台抽取比例(%)',
  `platform_extract_price` decimal(10,2) DEFAULT '0.00' COMMENT '平台抽取金额',
  `platform_delivery_fee` decimal(10,2) DEFAULT '0.00' COMMENT '平台承担配送费',
  `platform_income` decimal(10,2) DEFAULT '0.00' COMMENT '平台实际收入',
  `merchant_delivery_fee` decimal(10,2) DEFAULT '0.00' COMMENT '商家承担配送费',
  `courier_income` decimal(10,2) DEFAULT '0.00' COMMENT '骑手实际收入',
  `merchant_income` decimal(10,2) DEFAULT '0.00' COMMENT '商家实际收入',
  `payment_success_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `order_completion_time` datetime DEFAULT NULL COMMENT '订单完成时间',
  `paid_order_cancel_reason` int(2) DEFAULT NULL COMMENT '(废弃字段)(已支付)订单取消原因 1=信息填写错误 2=送达时间选错了 3=买错了/买少了 4=商家缺货 5=商家联系我取消 6=配送太慢 7=骑手联系我取消 8=我不想要了',
  `limited_price` decimal(10,2) DEFAULT '0.00' COMMENT '满足价格(元)',
  `reduced_price` decimal(10,2) DEFAULT '0.00' COMMENT '减价额度(元)',
  `coupons_discount_price` decimal(10,2) DEFAULT '0.00' COMMENT '优惠券折扣金额/优惠券优惠金额',
  `delivery_way` int(2) DEFAULT NULL COMMENT '配送方式 1=商家自配送',
  `is_pay_to_merchant` tinyint(1) DEFAULT '0' COMMENT '用户下单资金是否已转入商家余额 0=否 1=是',
  `before_reduced_delivery_fee` decimal(10,2) DEFAULT '0.00' COMMENT '立减之前需要支付的运费/配送费',
  `payment_mode` int(2) DEFAULT NULL COMMENT '支付方式 1=微信支付 (2=平台余额) 3=平台积分',
  `logistics_way` varchar(50) DEFAULT NULL COMMENT '物流方式/快递公司名称',
  `logistics_no` varchar(50) DEFAULT NULL COMMENT '物流编号/快递单号',
  `courier_name` varchar(50) DEFAULT NULL COMMENT '快递员/快递站名称',
  `courier_phone` varchar(11) DEFAULT NULL COMMENT '快递员电话',
  `delivery_status` int(2) DEFAULT NULL COMMENT '快递状态 0=快递收件(揽件) 1=在途中 2=正在派件 3=已签收 4=派送失败 5=疑难件 6=退件签收',
  `is_sign` tinyint(1) DEFAULT NULL COMMENT '是否签收',
  `delivery_last_update_time` datetime DEFAULT NULL COMMENT '快递轨迹信息最新时间',
  `take_time` varchar(50) DEFAULT NULL COMMENT '发货到收货消耗时长 (截止最新轨迹)',
  `contact_house_number` varchar(100) DEFAULT NULL COMMENT '门牌号',
  `contact_longitude` decimal(18,6) DEFAULT NULL COMMENT '经度',
  `contact_latitude` decimal(18,6) DEFAULT NULL COMMENT '纬度',
  `first_goods_main_image` varchar(128) DEFAULT NULL COMMENT '第一件商品的主图',
  `goods_source` int(2) DEFAULT NULL COMMENT '发货源 1=拼多多 2=淘宝 3=京东',
  `goods_source_order_no` varchar(50) DEFAULT NULL COMMENT '发货源订单编号',
  `platform_remark` varchar(1024) DEFAULT NULL COMMENT '平台备注',
  `create_time` datetime DEFAULT NULL COMMENT '下单时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_order
-- ----------------------------

-- ----------------------------
-- Table structure for tb_points_mall_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_order_detail`;
CREATE TABLE `tb_points_mall_order_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_id` bigint(11) NOT NULL COMMENT '订单id',
  `goods_id` int(11) NOT NULL COMMENT '商品id',
  `goods_name` varchar(50) NOT NULL COMMENT '商品名称',
  `main_image` varchar(128) NOT NULL COMMENT '商品主图',
  `spec_list` varchar(1024) DEFAULT NULL COMMENT '商品规格 JSON数据',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '价格',
  `number` int(11) DEFAULT '0' COMMENT '购买数量',
  `subtotal` decimal(10,2) DEFAULT '0.00' COMMENT '小计',
  `packing_charges` decimal(10,2) DEFAULT '0.00' COMMENT '单件商品的包装费',
  `is_used_coupons` tinyint(1) DEFAULT '0' COMMENT '该商品是否使用了优惠券 0=否 1=是',
  `coupons_discount_price` decimal(10,2) DEFAULT '0.00' COMMENT '优惠券折扣金额/优惠券优惠金额',
  `after_coupons_discount_price` decimal(10,2) DEFAULT '0.00' COMMENT '优惠券折扣之后的商品价格',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0=正常 1=删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_order_detail
-- ----------------------------

-- ----------------------------
-- Table structure for tb_points_mall_order_logistics
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_order_logistics`;
CREATE TABLE `tb_points_mall_order_logistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_id` bigint(11) NOT NULL COMMENT '订单id',
  `description` varchar(200) DEFAULT NULL COMMENT '物流状态描述',
  `description_time` datetime DEFAULT NULL COMMENT '物流状态描述对应的时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_order_logistics
-- ----------------------------

-- ----------------------------
-- Table structure for tb_points_mall_order_refund
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_order_refund`;
CREATE TABLE `tb_points_mall_order_refund` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_id` bigint(11) NOT NULL COMMENT '订单id',
  `type` int(2) DEFAULT '1' COMMENT '退款类型 1=未发货订单申请退款 2=已发货订单申请售后-仅退款 3=已发货订单申请售后-退货退款',
  `refund_way` int(2) DEFAULT '1' COMMENT '退款方式 1=全额退款 2=部分退款',
  `refund_reason` int(2) DEFAULT NULL COMMENT '退款原因 1=订单信息拍错（规格/尺码/颜色等） 2=我不想要了 3=地址/电话信息填写错误 4=没用/少用优惠 5=缺货 31=缺货 32=协商一致退款 33=未按约定时间发货 34=排错/多拍/不想要 35=其他 61=效果不好/不喜欢 62=材质不符 63=尺寸不符 64=外观破损 65=颜色/款式图案与描述不符 66=物流问题 67=和预期不符 68=服务承诺/态度 69=质量问题 70=其他',
  `refund_reason_description` varchar(1024) DEFAULT NULL COMMENT '退款原因详细描述',
  `evidence_images` varchar(1024) DEFAULT NULL COMMENT '凭证图片',
  `refund_amount` decimal(10,2) DEFAULT '0.00' COMMENT '退款金额',
  `refund_account` int(2) DEFAULT NULL COMMENT '退款账户 1=微信 2=支付宝 3=平台余额 4=平台积分',
  `status` int(2) DEFAULT '1' COMMENT '退款状态 1=退款申请已提交 4=等待平台处理 5=平台拒绝退款，退款已关闭 7=退款成功',
  `goods_total_quantity` int(11) DEFAULT '0' COMMENT '退还商品总数量',
  `goods_total_price` decimal(10,2) DEFAULT '0.00' COMMENT '退还商品总金额',
  `packing_charges` decimal(10,2) DEFAULT '0.00' COMMENT '退还包装费',
  `is_refund_delivery_fee` tinyint(1) DEFAULT '0' COMMENT '是否退还了配送费 0=否 1=是',
  `delivery_fee` decimal(10,2) DEFAULT '0.00' COMMENT '退还运费/配送费',
  `is_used_coupons` tinyint(1) DEFAULT '0' COMMENT '退款商品中是否使用了优惠券 0=否 1=是',
  `is_used_full_reduction_rule` tinyint(1) DEFAULT '0' COMMENT '退款商品中是否使用了满减规则 0=否 1=是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_order_refund
-- ----------------------------

-- ----------------------------
-- Table structure for tb_points_mall_order_refund_goods
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_order_refund_goods`;
CREATE TABLE `tb_points_mall_order_refund_goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_refund_id` int(11) NOT NULL COMMENT '订单退款id',
  `order_detail_id` int(11) NOT NULL COMMENT '订单商品详情id',
  `goods_id` int(11) NOT NULL COMMENT '商品id',
  `goods_name` varchar(50) NOT NULL COMMENT '商品名称',
  `main_image` varchar(128) NOT NULL COMMENT '商品主图',
  `spec_list` varchar(1024) DEFAULT NULL COMMENT '商品规格 JSON数据',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '价格',
  `number` int(11) DEFAULT '0' COMMENT '退款数量',
  `subtotal` decimal(10,2) DEFAULT '0.00' COMMENT '小计',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_order_refund_goods
-- ----------------------------

-- ----------------------------
-- Table structure for tb_points_mall_order_refund_process
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_order_refund_process`;
CREATE TABLE `tb_points_mall_order_refund_process` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_refund_id` int(11) NOT NULL COMMENT '订单退款id',
  `name` varchar(50) NOT NULL COMMENT '流程名称',
  `description` varchar(200) DEFAULT NULL COMMENT '流程描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_order_refund_process
-- ----------------------------

-- ----------------------------
-- Table structure for tb_points_mall_shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `tb_points_mall_shopping_cart`;
CREATE TABLE `tb_points_mall_shopping_cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `goods_id` int(11) NOT NULL COMMENT '商品id',
  `shop_id` int(11) DEFAULT NULL COMMENT '店铺id',
  `spec_list` varchar(1024) DEFAULT NULL COMMENT '商品规格 JSON数据',
  `number` int(11) DEFAULT '1' COMMENT '购买数量',
  `is_goods_exists` tinyint(1) DEFAULT '1' COMMENT '商品是否有效 0=无效 1=有效',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_points_mall_shopping_cart
-- ----------------------------
