create table nts_card_usaged_log
(
    id          int auto_increment
        primary key,
    card_id     int          null comment '物联卡id',
    create_time datetime     null comment '创建时间',
    update_time datetime     null comment '修改时间',
    date        date         null comment '记录时间',
    data_usage  varchar(32)  null comment '当前累计使用的数据量',
    remark      varchar(256) null comment '备注'
)
    comment '物联网卡使用日志' charset = utf8;

