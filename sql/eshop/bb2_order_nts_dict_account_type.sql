create table nts_dict_account_type
(
    account_type_id int(5) auto_increment comment '编号'
        primary key,
    name            varchar(100) default '' null comment '账号类别名称',
    descr           varchar(500) default '' null comment '账号类别描述',
    delete_flag     tinyint(1)   default 1  null comment '删除标志 1-正常状态 2-已删除',
    create_time     datetime                null comment '创建时间',
    update_time     datetime                null comment '更新时间'
)
    comment '字典-账号类别表' collate = utf8mb4_bin;

