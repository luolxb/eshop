create table nts_wallet
(
    wallet_id      bigint auto_increment comment '编号'
        primary key,
    company_id     bigint                 null comment '企业ID',
    money          decimal(20, 8)         null comment '金额',
    pay_password   varchar(64) default '' null comment '支付密码',
    salt           varchar(20) default '' null comment '盐加密',
    create_user_id bigint                 null comment '创建用户ID',
    update_user_id bigint                 null comment '修改用户ID',
    create_time    datetime               null comment '创建时间',
    update_time    datetime               null comment '更新时间'
)
    comment '企业钱包表' collate = utf8mb4_bin;

