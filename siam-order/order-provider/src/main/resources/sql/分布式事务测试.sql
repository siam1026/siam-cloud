#分布式事务测试-用户状态逻辑重置
UPDATE `siam_cloud`.tb_member SET is_new_people = TRUE, is_remind_new_people = TRUE WHERE mobile = '13121865386';

#分布式事务测试-购物车数据重置
INSERT INTO `siam_cloud`.`tb_points_mall_shopping_cart` (`id`, `member_id`, `goods_id`, `shop_id`, `spec_list`, `number`, `is_goods_exists`, `create_time`, `update_time`) VALUES ('124', '2', '7', NULL, '{}', '1', '1', NULL, NULL);

#分布式事务测试-订单表数据重置
TRUNCATE `siam_cloud`.`tb_points_mall_order`;


SELECT is_new_people, is_remind_new_people, tb_member.* FROM `siam_cloud`.tb_member WHERE mobile = '13121865386';

SELECT * FROM `siam_cloud`.`tb_points_mall_shopping_cart`;

SELECT * FROM `siam_cloud`.`tb_points_mall_order`;
