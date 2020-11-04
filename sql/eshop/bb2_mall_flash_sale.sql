create table flash_sale
(
    id           bigint(10) auto_increment
        primary key,
    title        varchar(200)            default ''                not null comment '活动标题',
    goods_id     int unsigned            default 0                 not null comment '参团商品ID',
    item_id      bigint unsigned         default 0                 not null comment '对应spec_goods_price商品规格id',
    price        decimal(10, 2) unsigned default 0.00              not null comment '活动价格',
    goods_num    int unsigned            default 1                 not null comment '商品参加活动数',
    buy_limit    int(11) unsigned        default 1                 not null comment '每人限购数',
    buy_num      int(11) unsigned        default 0                 not null comment '已购买人数',
    order_num    int unsigned            default 0                 not null comment '已下单数',
    description  text                                              not null comment '活动描述',
    start_time   int(11) unsigned                                  not null comment '开始时间',
    end_time     int(11) unsigned                                  not null comment '结束时间',
    is_end       tinyint(1) unsigned     default 0                 not null comment '是否已结束',
    goods_name   varchar(255)            default ''                not null comment '商品名称',
    store_id     int unsigned            default 0                 not null,
    recommend    tinyint(1) unsigned     default 0                 not null comment '是否推荐',
    status       tinyint(1) unsigned     default 0                 not null comment '抢购状态：1正常，0待审核，2审核拒绝，3关闭活动，4商品售馨',
    is_del       tinyint(1) unsigned     default 0                 not null comment '软删除',
    gmt_create   datetime                default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modified datetime                default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
)
    charset = utf8;

INSERT INTO bb2_mall.flash_sale (id, title, goods_id, item_id, price, goods_num, buy_limit, buy_num, order_num, description, start_time, end_time, is_end, goods_name, store_id, recommend, status, is_del, gmt_create, gmt_modified) VALUES (1, '贵州茅台白酒', 390, 0, 6.88, 1, 1, 0, 0, '中国白酒', 1598853600, 1598860800, 1, '八一特供陈酿贵州茅台酒', 89, 0, 1, 1, '2020-08-31 13:11:05', '2020-08-31 16:00:01');
INSERT INTO bb2_mall.flash_sale (id, title, goods_id, item_id, price, goods_num, buy_limit, buy_num, order_num, description, start_time, end_time, is_end, goods_name, store_id, recommend, status, is_del, gmt_create, gmt_modified) VALUES (2, '珍藏白酒', 391, 0, 0.88, 1, 1, 0, 0, '价格优惠,先到先得', 1598853600, 1598860800, 1, '八一特供陈酿贵州茅台酒', 89, 1, 1, 1, '2020-08-31 13:31:27', '2020-08-31 16:00:01');
INSERT INTO bb2_mall.flash_sale (id, title, goods_id, item_id, price, goods_num, buy_limit, buy_num, order_num, description, start_time, end_time, is_end, goods_name, store_id, recommend, status, is_del, gmt_create, gmt_modified) VALUES (3, '秒杀活动', 394, 0, 0.99, 1, 1, 0, 0, '第一次秒杀活动', 1598925600, 1598932800, 0, '八一特供陈酿贵州茅台酒', 89, 1, 1, 0, '2020-09-01 09:06:09', '2020-09-01 09:06:29');