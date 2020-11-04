create table store_collect
(
    log_id     int unsigned auto_increment
        primary key,
    user_id    int(10)      null,
    store_id   int(10)      null,
    add_time   int          null comment '收藏店铺时间',
    store_name varchar(100) null,
    user_name  varchar(50)  null comment '收藏会员名称'
)
    charset = utf8;

INSERT INTO bb2_user.store_collect (log_id, user_id, store_id, add_time, store_name, user_name) VALUES (105, 373, 123, 1600739870, '13052050250', '13066803937');
INSERT INTO bb2_user.store_collect (log_id, user_id, store_id, add_time, store_name, user_name) VALUES (123, 364, 124, 1600830217, '13066803937', '13058140233');
INSERT INTO bb2_user.store_collect (log_id, user_id, store_id, add_time, store_name, user_name) VALUES (124, 371, 115, 1600843052, '18687269789', '15015102661');