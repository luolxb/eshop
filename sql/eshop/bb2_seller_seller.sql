create table seller
(
    seller_id        int unsigned auto_increment comment '卖家编号'
        primary key,
    seller_name      varchar(50)         default '' not null comment '卖家用户名',
    user_id          int unsigned                   null comment '用户编号',
    group_id         int unsigned                   null comment '卖家组编号',
    store_id         int unsigned                   null comment '店铺编号',
    is_admin         tinyint unsigned    default 0  not null comment '是否管理员(0-不是 1-是)',
    seller_quicklink varchar(255)                   null comment '卖家快捷操作',
    last_login_time  int(11) unsigned               null comment '最后登录时间',
    enabled          tinyint(1)          default 0  null comment '激活状态 0启用1关闭',
    add_time         int                            null,
    token            varchar(64)                    null comment '用于app 授权类似于session_id',
    open_teach       tinyint(1) unsigned default 0  not null comment '是否显示新手向导'
)
    comment '卖家用户表' charset = utf8;

INSERT INTO bb2_seller.seller (seller_id, seller_name, user_id, group_id, store_id, is_admin, seller_quicklink, last_login_time, enabled, add_time, token, open_teach) VALUES (1, 'seller', 2, null, 1, 1, 'Finance_order_no_statis,Goods_addStepOne', 1596778326, 0, null, '', 0);
INSERT INTO bb2_seller.seller (seller_id, seller_name, user_id, group_id, store_id, is_admin, seller_quicklink, last_login_time, enabled, add_time, token, open_teach) VALUES (54, '13056051048', 361, 243, 113, 1, null, 1601371743, 0, null, null, 0);
INSERT INTO bb2_seller.seller (seller_id, seller_name, user_id, group_id, store_id, is_admin, seller_quicklink, last_login_time, enabled, add_time, token, open_teach) VALUES (55, '18687253458', 362, 244, 114, 1, null, 1600250193, 0, null, null, 0);
INSERT INTO bb2_seller.seller (seller_id, seller_name, user_id, group_id, store_id, is_admin, seller_quicklink, last_login_time, enabled, add_time, token, open_teach) VALUES (56, '18603059852', 368, 245, 119, 1, null, 1601371182, 0, null, null, 0);
INSERT INTO bb2_seller.seller (seller_id, seller_name, user_id, group_id, store_id, is_admin, seller_quicklink, last_login_time, enabled, add_time, token, open_teach) VALUES (57, '13098040987', 369, 246, 120, 1, null, 1600848760, 0, null, null, 0);
INSERT INTO bb2_seller.seller (seller_id, seller_name, user_id, group_id, store_id, is_admin, seller_quicklink, last_login_time, enabled, add_time, token, open_teach) VALUES (58, '13089098909', 370, 247, 121, 1, null, 1601371113, 0, null, null, 0);
INSERT INTO bb2_seller.seller (seller_id, seller_name, user_id, group_id, store_id, is_admin, seller_quicklink, last_login_time, enabled, add_time, token, open_teach) VALUES (59, '13052050250', 372, 248, 123, 1, null, 1602221097, 0, null, null, 0);
INSERT INTO bb2_seller.seller (seller_id, seller_name, user_id, group_id, store_id, is_admin, seller_quicklink, last_login_time, enabled, add_time, token, open_teach) VALUES (60, '13052635240', 374, 249, 125, 1, null, 1601371089, 0, null, null, 0);