create table nts_wallet_record
(
    wallet_record_id bigint auto_increment comment '编号'
        primary key,
    company_id       bigint                  null comment '企业ID',
    user_id          bigint                  null comment '用户ID',
    wallet_record_sn varchar(64)  default '' null comment '交易流水sn，ymdHis+2位随机数',
    from_uid         varchar(64)  default '' null comment '支付方UID，0-系统充值',
    to_uid           varchar(64)  default '' null comment '接收方UID，0-系统提现',
    type             tinyint(1)   default 1  null comment '交易类型 1-充值 2-提现 3-交易',
    money            decimal(20, 8)          null comment '交易金额',
    pay_model_id     int(5)       default 0  null comment '支付方式ID 0-待定 支付宝|微信|银行卡|余额',
    remark           varchar(500) default '' null comment '备注',
    pay_status       tinyint(1)   default 0  null comment '支付状态 0-待支付 -1-失败 1-成功',
    pay_time         datetime                null comment '交易时间',
    fetch_status     tinyint(1)   default 0  null comment '收款状态 0-待收款 -1-失败 1-成功',
    fetch_time       datetime                null comment '收款时间',
    check_status     tinyint(1)   default 0  null comment '对账状态 0-未对账 1-已对账',
    check_time       datetime                null comment '对账时间',
    create_time      datetime                null comment '创建时间'
)
    comment '钱包交易记录表' collate = utf8mb4_bin;

