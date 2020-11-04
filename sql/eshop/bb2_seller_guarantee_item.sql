create table guarantee_item
(
    grt_id         int auto_increment comment '自增ID'
        primary key,
    grt_name       varchar(100)                not null comment '保障项目名称',
    grt_describe   varchar(2000)               not null comment '保障项目描述',
    grt_cost       decimal(10, 2) default 0.00 not null comment '保证金',
    grt_icon       varchar(500)                not null comment '图标',
    grt_descurl    varchar(500)                null comment '内容介绍文章地址',
    grt_state      tinyint(1)     default 1    not null comment '状态 0关闭 1开启',
    grt_sort       int            default 0    null comment '排序',
    grt_charge     varchar(255)                null comment '收费规则',
    grt_compensate varchar(255)                null comment '理赔规则',
    grt_money      decimal(10, 2) default 0.00 null comment '赔付金额',
    grt_score      int(10)        default 0    null comment '扣除分数'
)
    comment '消费者保障服务项目表' charset = utf8;

INSERT INTO bb2_seller.guarantee_item (grt_id, grt_name, grt_describe, grt_cost, grt_icon, grt_descurl, grt_state, grt_sort, grt_charge, grt_compensate, grt_money, grt_score) VALUES (1, '满额包邮', '8555', 1000.00, '', 'http://bb2t.tp-shop.cn/index.php/Home/Goods/goodsInfo/id/1.html', 0, 3, null, null, 0.00, 0);
INSERT INTO bb2_seller.guarantee_item (grt_id, grt_name, grt_describe, grt_cost, grt_icon, grt_descurl, grt_state, grt_sort, grt_charge, grt_compensate, grt_money, grt_score) VALUES (2, '无忧退货', '七天内可无理由退换货', 50.00, '', '', 1, 6, null, null, 0.00, 0);