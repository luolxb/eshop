create table nts_dict_company_member
(
    company_member_id int(5) auto_increment comment '编号'
        primary key,
    name              varchar(100) default '' null comment '企业会员名称',
    descr             varchar(500) default '' null comment '企业会员描述',
    delete_flag       tinyint(1)   default 1  null comment '删除标志 1-正常状态 2-已删除',
    create_time       datetime                null comment '创建时间',
    update_time       datetime                null comment '更新时间'
)
    comment '字典-企业会员表' collate = utf8mb4_bin;

