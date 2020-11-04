create table nts_company_flow_pool
(
    company_flow_pool_id bigint auto_increment comment '编号'
        primary key,
    company_id           bigint               null comment '企业ID',
    surplus_flow         double(20, 4)        null comment '剩余流量',
    used_flow            double(20, 4)        null comment '已用流量',
    delete_flag          tinyint(1) default 1 null comment '删除标志 1-正常状态 2-已删除',
    create_time          datetime             null comment '创建时间',
    update_time          datetime             null comment '更新时间',
    constraint company_id
        unique (company_id)
)
    comment '企业流量池表';

