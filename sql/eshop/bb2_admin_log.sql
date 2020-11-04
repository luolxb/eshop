create table log
(
    log_id   bigint(16) unsigned auto_increment
        primary key,
    admin_id int(10)              null,
    log_info varchar(255)         null,
    log_ip   varchar(30)          null,
    log_url  varchar(255)         null,
    log_time int(10)              null,
    log_type tinyint(2) default 0 null comment '0默认1操作店铺2审核活动3处理投诉4其他'
)
    charset = utf8;

