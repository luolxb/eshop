create table combination_goods
(
    combination_id int unsigned            default 0    not null,
    goods_name     varchar(255)            default ''   not null comment '商品名称',
    key_name       varchar(255)            default ''   not null comment '规格名称',
    goods_id       int unsigned            default 0    not null,
    item_id        int unsigned            default 0    not null,
    original_price decimal(10, 2) unsigned default 0.00 not null comment '原价/商城价',
    price          decimal(10, 2) unsigned default 0.00 not null comment '优惠价格',
    is_master      tinyint(1) unsigned     default 0    not null comment '1主0从'
)
    comment '组合促销商品映射关系表' engine = MyISAM
                          charset = utf8;

