create table nts_notice_user_tag
(
    id           bigint auto_increment comment '编号'
        primary key,
    notice_id    bigint               null comment '通告编号',
    user_id      bigint               null comment '用户编号',
    is_read      tinyint(1) default 0 null comment '是否阅读 0-未阅读 1-已阅读',
    read_time    datetime             null comment '阅读时间',
    account_type int(5)               null comment '用户类型'
)
    comment '通告用户标记表';

