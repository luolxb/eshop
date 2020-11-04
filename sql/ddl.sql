-- 新增提货订单
-- 新增人：罗先波
-- 新增时间：2020-09-08
CREATE TABLE `pick_order` (
  `pick_order_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '提货订单id',
  `order_id` int(10) DEFAULT NULL COMMENT '购买订单id',
  `add_time` int(10) DEFAULT NULL COMMENT '申请提货时间',
  `pick_order_status` int(2) DEFAULT NULL COMMENT '提货订单状态',
  `user_id` int(10) DEFAULT NULL COMMENT '提货人',
  `seller_id` int(10) DEFAULT NULL COMMENT '发货人',
  `pick_order_sn` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '提货订单编码',
  `consignee_id` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收货人id',
  `country` int(10) DEFAULT NULL COMMENT '国家',
  `province` int(10) DEFAULT NULL COMMENT '省',
  `city` int(10) DEFAULT NULL COMMENT '市',
  `district` int(10) DEFAULT NULL COMMENT '区',
  `twon` int(10) DEFAULT NULL COMMENT '街道',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `zipcode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮编',
  `mobile` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `shipping_code` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流code',
  `shipping_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物流名称',
  `shipping_price` decimal(10,2) DEFAULT NULL COMMENT '邮费',
  `shipping_time` int(10) DEFAULT NULL COMMENT '物流最新时间',
  `goods_id` int(10) DEFAULT NULL COMMENT '商品id',
  `update_time` int(10) DEFAULT NULL COMMENT '提货状态时间',
  `nickname` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '提货人',
  PRIMARY KEY (`pick_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 新增提货订单操作记录
-- 新增人：罗先波
-- 新增时间：2020-09-08
CREATE TABLE `pick_order_action` (
  `prick_order_action_id` MEDIUMINT(8) UNSIGNED NOT NULL AUTO_INCREMENT,
  `prick_order_id` MEDIUMINT(8) UNSIGNED NOT NULL DEFAULT '0' COMMENT '提货订单ID',
  `action_user` INT(11) NOT NULL DEFAULT '0' COMMENT '操作人 0 为用户操作，其他为管理员id',
  `pick_order_status` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0',
  `action_note` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '操作备注',
  `log_time` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '操作时间',
  `status_desc` VARCHAR(255) DEFAULT NULL COMMENT '状态描述',
  `user_type` TINYINT(1) DEFAULT '0' COMMENT '0管理员1商家2前台用户',
  `store_id` INT(11) DEFAULT '0' COMMENT '商家店铺ID',
  PRIMARY KEY (`prick_order_action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 商品表新增删除标记
-- 新增人：罗先波
-- 新增时间：2020-09-08
ALTER TABLE `bb2_mall`.`goods` DROP COLUMN `delete_flag`, ADD COLUMN `delete_flag` TINYINT(1) DEFAULT 0 NULL COMMENT '删除标记 0：正常   1：删除' AFTER `owner_id`;

-- 商店表新增交易数量
-- 新增人：罗先波
-- 新增时间：2020-09-09
ALTER TABLE `bb2_seller`.`store` ADD COLUMN `transaction_num` INT(10) NULL COMMENT '交易数量' AFTER `store_notice`;


-- 修改商店表新增交易数量 初始默认值0
-- 新增人：罗先波
-- 新增时间：2020-09-09
ALTER TABLE `bb2_seller`.`store` CHANGE `transaction_num` `transaction_num` INT(10) DEFAULT 0 NULL COMMENT '交易数量';



-- 存证表新增字段
-- 新增人：罗先波
-- 新增时间：2020-09-11
ALTER TABLE `bb2_seller`.`deposit_certificate` ADD COLUMN `fragrance` VARCHAR(64) NULL COMMENT '香型' AFTER `owner_id`, ADD COLUMN `degree` VARCHAR(64) NULL COMMENT '度数' AFTER `fragrance`, ADD COLUMN `capacity` VARCHAR(64) NULL COMMENT '容量' AFTER `degree`;

-- 提货订单表新增字段
-- 新增人：罗先波
-- 新增时间：2020-09-12
ALTER TABLE `bb2_order`.`pick_order` ADD COLUMN `pick_order_type` INT(1) NULL COMMENT '提货类型 0:手工单号 1. 预约到店 2. 电子面单 3. 无需物流' AFTER `nickname`;