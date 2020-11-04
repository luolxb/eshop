create table seller_log
(
    log_id          int unsigned auto_increment comment '日志编号'
        primary key,
    log_content     varchar(50)      default '' not null comment '日志内容',
    log_time        int unsigned                not null comment '日志时间',
    log_seller_id   int unsigned                not null comment '卖家编号',
    log_seller_name varchar(50)      default '' not null comment '卖家帐号',
    log_store_id    int unsigned                not null comment '店铺编号',
    log_seller_ip   varchar(50)      default '' not null comment '卖家ip',
    log_url         varchar(50)      default '' not null comment '日志url',
    log_state       tinyint unsigned default 1  not null comment '日志状态(0-失败 1-成功)'
)
    comment '卖家日志表' charset = utf8;

