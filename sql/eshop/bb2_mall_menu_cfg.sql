create table menu_cfg
(
    menu_id      smallint(4) unsigned auto_increment
        primary key,
    menu_name    varchar(100)        default '' not null comment '自定义名称',
    default_name varchar(100)        default '' not null comment '默认名称',
    is_show      tinyint(1) unsigned default 0  not null comment '是否显示'
)
    charset = utf8;

INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (1, '订单列表', '我的订单', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (2, '优惠券', '我的优惠券', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (3, '海报', '我的海报', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (4, '我的分销', '我的分销', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (5, '虚拟订单', '虚拟订单', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (6, '拼团订单', '拼团订单', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (7, '预约订单', '预约订单', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (8, '自提订单', '自提订单', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (9, '我的评价', '我的评价', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (10, '积分兑换', '积分兑换', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (11, '我的签到', '我的签到', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (12, '领券中心', '领券中心', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (13, '我的收藏', '我的收藏', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (14, '我的足迹', '我的足迹', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (15, '我的消息', '我的消息', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (17, '地址管理', '地址管理', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (18, '砍价订单', '砍价订单', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (19, '使用帮助', '使用帮助', 1);
INSERT INTO bb2_mall.menu_cfg (menu_id, menu_name, default_name, is_show) VALUES (20, '我的购物卡', '我的购物卡', 0);