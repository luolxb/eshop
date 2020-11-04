create table group_buy_goods_item
(
    id           int unsigned auto_increment
        primary key,
    group_buy_id int unsigned default 0  not null comment '团购id',
    goods_id     int unsigned default 0  not null comment '商品id',
    item_id      bigint(10)   default 0  not null comment '对应spec_goods_price商品规格id',
    price        decimal(10, 2)          not null comment '团购价格',
    goods_num    int unsigned default 0  not null comment '商品参团数',
    virtual_num  int          default 0  not null comment '虚拟购买数',
    rebate       decimal(10, 1)          not null comment '折扣',
    goods_price  decimal(10, 2) unsigned not null comment '商品原价',
    buy_num      int unsigned default 0  not null comment '购买数量',
    order_num    int          default 0  not null comment '已下单人数'
)
    comment '团购活动商品表' engine = MyISAM
                      charset = utf8mb4;

