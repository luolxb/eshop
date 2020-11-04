create table group_buy
(
    id           int unsigned auto_increment comment '团购ID'
        primary key,
    title        varchar(255)            default ''                not null comment '活动名称',
    start_time   int unsigned            default 0                 not null comment '开始时间',
    end_time     int unsigned            default 0                 not null comment '结束时间',
    goods_id     int unsigned            default 0                 not null comment '商品ID',
    item_id      bigint                  default 0                 null comment '对应spec_goods_price商品规格id',
    price        decimal(10, 2) unsigned default 0.00              not null comment '团购价格',
    goods_num    int(10)                 default 0                 null comment '商品参团数',
    buy_num      int unsigned            default 0                 not null comment '商品已购买数',
    order_num    int unsigned            default 0                 not null comment '已下单人数',
    virtual_num  int unsigned            default 0                 not null comment '虚拟购买数',
    rebate       decimal(10, 1) unsigned default 0.0               not null comment '折扣',
    intro        text                                              null comment '本团介绍',
    goods_price  decimal(10, 2) unsigned default 0.00              not null comment '商品原价',
    goods_name   varchar(200)            default ''                not null comment '商品名称',
    recommend    tinyint(1) unsigned     default 0                 not null comment '是否推荐 0.未推荐 1.已推荐',
    views        int unsigned            default 0                 not null comment '查看次数',
    store_id     int(10)                 default 0                 null comment '商家店铺ID',
    status       tinyint(1)              default 0                 null comment '团购状态，0待审核，1正常2拒绝3关闭',
    is_end       tinyint(1)              default 0                 not null comment '是否结束,1结束 ，0正常',
    is_deleted   tinyint(1)              default 0                 not null comment '软删除',
    gmt_create   datetime                default CURRENT_TIMESTAMP null comment '创建时间',
    gmt_modified datetime                default CURRENT_TIMESTAMP null comment '更新时间'
)
    comment '团购商品表' charset = utf8;

