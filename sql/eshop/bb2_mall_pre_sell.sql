create table pre_sell
(
    pre_sell_id        int(11) unsigned auto_increment comment '预售id'
        primary key,
    goods_id           mediumint unsigned        default 0                 not null comment '商品id',
    goods_name         varchar(255) charset utf8 default ''                not null comment '商品名称',
    item_id            bigint unsigned           default 0                 not null comment '规格id',
    item_name          varchar(255) charset utf8 default ''                not null comment '规格名称',
    title              varchar(255) charset utf8 default ''                not null comment '预售标题',
    `desc`             varchar(255) charset utf8 default ''                not null comment '预售描述',
    deposit_goods_num  int unsigned              default 0                 not null comment '订购商品数',
    deposit_order_num  int unsigned              default 0                 not null comment '订购订单数',
    stock_num          smallint unsigned         default 0                 not null comment '预售库存',
    sell_start_time    int unsigned              default 0                 not null comment '活动开始时间',
    sell_end_time      int unsigned              default 0                 not null comment '活动结束时间',
    pay_start_time     int unsigned              default 0                 not null comment '尾款支付开始时间',
    pay_end_time       int unsigned              default 0                 not null comment '尾款支付结束时间',
    deposit_price      decimal(10, 2) unsigned   default 0.00              not null comment '订金',
    price_ladder       varchar(255) charset utf8 default ''                not null comment '价格阶梯。预定人数达到多少个时，价格为多少钱',
    delivery_time_desc varchar(255) charset utf8 default ''                not null comment '开始发货时间描述',
    store_id           int(11) unsigned          default 0                 not null comment '店铺id',
    is_finished        tinyint(1) unsigned       default 0                 not null comment '是否已结束:0,正常；1，结束（待处理）；2,成功结束；3，失败结束。',
    status             tinyint(1) unsigned       default 0                 not null comment '团购状态，0待审核，1正常2拒绝3关闭',
    is_deleted         tinyint(1) unsigned       default 0                 not null comment '软删除',
    gmt_create         datetime                  default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modified       datetime                  default CURRENT_TIMESTAMP not null comment '更新时间'
)
    charset = latin1;

