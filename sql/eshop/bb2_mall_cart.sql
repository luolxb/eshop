create table cart
(
    id                   int(8) unsigned auto_increment comment '购物车表'
        primary key,
    user_id              mediumint unsigned         default 0    not null comment '用户id',
    session_id           char(128) collate utf8_bin default ''   not null comment 'session',
    goods_id             mediumint unsigned         default 0    not null comment '商品id',
    goods_sn             varchar(60)                default ''   not null comment '商品货号',
    goods_name           varchar(120)               default ''   not null comment '商品名称',
    market_price         decimal(10, 2) unsigned    default 0.00 not null comment '市场价',
    goods_price          decimal(10, 2)             default 0.00 not null comment '本店价',
    member_goods_price   decimal(10, 2) unsigned    default 0.00 not null comment '会员折扣价',
    goods_num            smallint unsigned          default 0    not null comment '购买数量',
    spec_key             varchar(64)                default ''   not null comment '商品规格key 对应tp_spec_goods_price 表',
    spec_key_name        varchar(64)                default ''   not null comment '商品规格组合名称',
    bar_code             varchar(64)                default ''   not null comment '商品条码',
    selected             tinyint(1) unsigned        default 1    not null comment '购物车选中状态',
    add_time             int(11) unsigned           default 0    not null comment '加入购物车的时间',
    prom_type            tinyint(1) unsigned        default 0    not null comment '0 普通订单,1 限时抢购, 2 团购 , 3 促销优惠',
    prom_id              int(11) unsigned           default 0    not null comment '活动id',
    sku                  varchar(128)               default ''   not null comment 'sku',
    store_id             int unsigned               default 0    not null comment '商家店铺ID',
    shop_id              int(11) unsigned           default 0    not null comment '门店ID',
    sgs_id               int(11) unsigned           default 0    not null comment '门店商品表ID',
    item_id              int(11) unsigned           default 0    not null comment '规格ID',
    combination_group_id int(8) unsigned            default 0    not null comment '搭配购的组id/cart_id',
    cart_store_id        int(11) unsigned           default 0    not null comment '属于哪个店铺的购物车，store_id是记哪个店铺的商品'
)
    charset = utf8;

create index goods_id
    on cart (goods_id);

create index session_id
    on cart (session_id);

create index user_id
    on cart (user_id);

