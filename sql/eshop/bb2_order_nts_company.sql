create table nts_company
(
    company_id        bigint auto_increment comment '编号'
        primary key,
    name              varchar(200) default '' null comment '企业名称',
    alias             varchar(100) default '' null comment '企业别名',
    address           varchar(500) default '' null comment '企业地址',
    industry_id       int(5)                  null comment '行业ID',
    company_member_id int(5)                  null comment '企业会员ID',
    company_scale_id  int(5)                  null comment '企业规模ID',
    lic_code          varchar(100) default '' null comment '信用代码|注册号',
    lic_name          varchar(200) default '' null comment '营业执照企业名称',
    lic_address       varchar(500) default '' null comment '营业执照企业地址',
    legal_person      varchar(100) default '' null comment '企业法人',
    license           varchar(500) default '' null comment '营业执照Url',
    web_url           varchar(500) default '' null comment '企业网站Url',
    email             varchar(100) default '' null comment '企业邮箱',
    telephone         varchar(100) default '' null comment '座机电话',
    audit_status      tinyint(1)   default 0  null comment '审核状态：0-未审核 1-审核成功 2-审核失败',
    delete_flag       tinyint(1)   default 1  null comment '删除标志 1-正常状态 2-已删除',
    audit_desc        varchar(500) default '' null comment '审核说明',
    create_time       datetime                null comment '创建时间',
    update_time       datetime                null comment '更新时间'
)
    comment '企业表' collate = utf8mb4_bin;

