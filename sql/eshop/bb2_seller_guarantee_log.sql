create table guarantee_log
(
    log_id        int auto_increment comment '自增ID'
        primary key,
    log_storeid   int           not null comment '店铺ID',
    log_storename varchar(500)  not null comment '店铺名称',
    log_grtid     int           not null comment '服务项目ID',
    log_grtname   varchar(100)  not null comment '服务项目名称',
    log_msg       varchar(1000) not null comment '操作描述',
    log_addtime   int           not null comment '添加时间',
    log_role      varchar(100)  not null comment '操作者角色 管理员为admin 商家为seller',
    log_userid    int           not null comment '操作者ID',
    log_username  varchar(200)  not null comment '操作者名称'
)
    comment '店铺消费者保障服务日志表' charset = utf8;

