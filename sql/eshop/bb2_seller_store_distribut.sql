create table store_distribut
(
    id          int auto_increment comment '表id自增'
        primary key,
    switch      tinyint(1) default 0  null comment '分销开关',
    `condition` tinyint(1) default 0  null comment '成为分销商条件 0 直接成为分销商,1成功购买商品后成为分销商',
    regrade     tinyint(1) default 0  null comment '返佣级数0一级1两级2三级',
    first_rate  tinyint(1) default 0  null comment '一级分销商比例',
    second_rate tinyint(1) default 0  null comment '二级分销商名称',
    third_rate  tinyint(1) default 0  null comment '三级分销商名称',
    date        tinyint(1) default 15 null comment '订单收货确认后多少天可以分成',
    store_id    int        default 0  null comment '店铺id'
)
    charset = utf8;

INSERT INTO bb2_seller.store_distribut (id, switch, `condition`, regrade, first_rate, second_rate, third_rate, date, store_id) VALUES (1, 0, 0, 1, 10, 8, 3, 15, 4);
INSERT INTO bb2_seller.store_distribut (id, switch, `condition`, regrade, first_rate, second_rate, third_rate, date, store_id) VALUES (2, 0, 0, 0, 0, 0, 0, 15, 1);