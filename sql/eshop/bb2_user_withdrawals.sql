create table withdrawals
(
    id          int auto_increment comment '提现申请表'
        primary key,
    user_id     int                           default 0    null comment '用户id',
    money       decimal(10, 2)                default 0.00 null comment '提现金额',
    create_time int                           default 0    null comment '申请时间',
    check_time  int                           default 0    null comment '审核时间',
    pay_time    int                           default 0    null comment '支付时间',
    refuse_time int                           default 0    null comment '拒绝时间',
    bank_name   varchar(255) collate utf8_bin default ''   null comment '银行名称 如支付宝 微信 中国银行 农业银行等',
    bank_card   varchar(255) collate utf8_bin default ''   null comment '银行账号或支付宝账号',
    realname    varchar(100) collate utf8_bin default ''   null comment '提款账号真实姓名',
    remark      varchar(255) collate utf8_bin default ''   null comment '提现备注',
    taxfee      decimal(10, 2)                default 0.00 null comment '税收手续费',
    status      tinyint(1)                    default 0    null comment '状态：-2删除作废-1审核失败0申请中1审核通过2付款成功3付款失败',
    pay_code    varchar(100)                               null comment '付款对账流水号',
    error_code  varchar(255)                               null comment '付款失败错误代码',
    type        tinyint(1) unsigned           default 0    not null comment '提箱类型:0余额提现,1佣金提现'
)
    charset = utf8;

