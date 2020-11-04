create table suppliers
(
    suppliers_id       smallint unsigned auto_increment comment '供应商ID'
        primary key,
    suppliers_name     varchar(255)        default '' not null comment '供应商名称',
    suppliers_desc     mediumtext                     not null comment '供应商描述',
    is_check           tinyint(1) unsigned default 1  not null comment '供应商状态',
    suppliers_contacts varchar(255)        default '' not null comment '供应商联系人',
    suppliers_phone    varchar(20)         default '' not null comment '供应商电话',
    store_id           int(10)             default 0  null comment '所属商家id'
)
    charset = utf8;

