create table goods_attr
(
    goods_attr_id int unsigned auto_increment comment '商品属性id自增'
        primary key,
    goods_id      int(11) unsigned default 0  not null comment '商品id',
    attr_id       int(11) unsigned default 0  not null comment '属性id',
    attr_value    text                        not null comment '属性值',
    attr_price    varchar(255)     default '' not null comment '属性价格'
)
    charset = utf8;

create index attr_id
    on goods_attr (attr_id);

create index goods_id
    on goods_attr (goods_id);

