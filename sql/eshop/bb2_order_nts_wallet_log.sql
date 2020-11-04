create table nts_wallet_log
(
    wallet_log_id    bigint auto_increment comment '编号'
        primary key,
    company_id       bigint                  null comment '企业ID',
    user_id          bigint                  null comment '用户ID',
    wallet_record_sn varchar(64)  default '' null comment '交易流水sn，关联nts_wallet_record表',
    change_money     decimal(20, 8)          null comment '变动金额 增+ 减-',
    money            decimal(20, 8)          null comment '变动后的金额',
    remark           varchar(500) default '' null comment '备注',
    display          tinyint(1)   default 1  null comment '是否显示 0-不显示 1-显示',
    create_time      datetime                null comment '创建时间'
)
    comment '钱包变动日志表' collate = utf8mb4_bin;

