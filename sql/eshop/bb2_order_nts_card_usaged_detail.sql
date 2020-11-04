create table nts_card_usaged_detail
(
    id          int auto_increment
        primary key,
    card_id     int          null comment '物联卡id',
    create_time datetime     null comment '创建时间',
    update_time datetime     null comment '修改时间',
    date        date         null comment '记录时间',
    data_usage  varchar(32)  null comment '使用的数据量',
    remark      varchar(256) null comment '备注'
)
    comment '物联卡流量使用使用详情' charset = utf8;

