create table store_msg
(
    sm_id    int unsigned auto_increment comment '店铺消息id'
        primary key,
    store_id int unsigned            not null comment '店铺id',
    content  varchar(512) default '' not null comment '消息内容',
    addtime  int unsigned            not null comment '发送时间',
    open     tinyint(1)   default 0  not null comment '消息是否已被查看'
)
    comment '店铺消息表' charset = utf8;

