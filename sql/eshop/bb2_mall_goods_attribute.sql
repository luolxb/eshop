create table goods_attribute
(
    attr_id         int(11) unsigned auto_increment comment '属性id'
        primary key,
    attr_name       varchar(60)         default '' not null comment '属性名称',
    type_id         smallint unsigned   default 0  not null comment '属性分类id',
    attr_index      tinyint(1) unsigned default 0  not null comment '0不需要检索 1关键字检索',
    attr_type       tinyint(1) unsigned default 0  not null comment '下拉框展示给商家选择',
    attr_input_type tinyint(1) unsigned default 2  not null comment '2多行文本框,平台属性录入方式',
    attr_values     text                           not null comment '可选值列表',
    `order`         tinyint unsigned    default 50 not null comment '属性排序'
)
    charset = utf8;

create index cat_id
    on goods_attribute (type_id);

