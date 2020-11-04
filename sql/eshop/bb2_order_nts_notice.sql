create table nts_notice
(
    notice_id       bigint auto_increment comment '编号'
        primary key,
    title           varchar(200) default '' null comment '通告标题',
    content         varchar(500) default '' null comment '通告内容',
    release_time    datetime                null comment '发布时间',
    is_undo         tinyint(1)   default 0  null comment '是否撤销 0-默认 1-撤销',
    undo_time       datetime                null comment '撤销时间',
    is_del          tinyint(1)   default 0  null comment '是否删除 0-默认 1-删除',
    del_time        datetime                null comment '删除时间',
    priority        tinyint(1)   default 0  null comment '优先级 0-默认 1-紧急 2-高 3-普通',
    notice_obj_type tinyint(1)   default 0  null comment '通告对象类型 0-默认 1-单个用户 2-多个用户 3-全部用户 4-用户类型',
    account_type    int(5)                  null comment '用户类型'
)
    comment '通告表';

