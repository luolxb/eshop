create table nts_user
(
    user_id         bigint auto_increment comment '编号'
        primary key,
    username        varchar(64)  default '' null comment '用户名',
    password        varchar(200) default '' null comment '密码',
    salt            varchar(20)  default '' null comment '盐加密',
    nickname        varchar(64)  default '' null comment '昵称',
    mobile          varchar(20)  default '' null comment '手机号',
    gender          tinyint(1)   default 1  null comment '性别 1-男生，2-女生',
    avatar          varchar(500) default '' null comment '头像',
    account_type_id int(5)                  null comment '用户类别ID',
    company_id      bigint                  null comment '企业ID',
    weixin_openid   varchar(200) default '' null comment '微信openId',
    status          tinyint(1)   default 1  null comment '状态 1-正常 2-暂停',
    delete_flag     tinyint(1)   default 1  null comment '删除标志 1-正常状态 2-已删除',
    register_ip     varchar(64)  default '' null comment '注册IP',
    register_time   datetime                null comment '注册时间',
    last_login_ip   varchar(64)  default '' null comment '最后登录IP',
    last_login_time datetime                null comment '最后登录时间',
    create_time     datetime                null comment '创建时间',
    update_time     datetime                null comment '更新时间',
    primary_account varchar(64)             null comment '企业主账号',
    mail_leader     varchar(64)             null comment '负责人邮箱',
    business_leader varchar(64)             null comment '业务负责人',
    is_admin        int          default 2  null comment '是否是管理员 1：是 2 :不是'
)
    comment '企业用户表' collate = utf8mb4_bin;

