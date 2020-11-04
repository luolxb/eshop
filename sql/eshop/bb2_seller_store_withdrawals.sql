create table store_withdrawals
(
    id          int auto_increment comment '商家提现申请表'
        primary key,
    store_id    int                           default 0    null comment '商家id',
    create_time int                           default 0    null comment '申请日期',
    refuse_time int                           default 0    null comment '拒绝时间',
    pay_time    int                           default 0    null comment '支付时间',
    check_time  int                           default 0    null comment '审核时间',
    money       decimal(10, 2)                default 0.00 null comment '提现金额',
    bank_name   varchar(100) collate utf8_bin default ''   null comment '银行名称 如支付宝 微信 中国银行 农业银行等',
    bank_card   varchar(100) collate utf8_bin default ''   null comment '银行账号',
    realname    varchar(100) collate utf8_bin default ''   null comment '银行账户名 可以是支付宝可以其他银行',
    remark      varchar(255) collate utf8_bin default ''   null comment '提现备注',
    status      tinyint(1)                    default 0    null comment '状态：-2删除作废-1审核失败0申请中1审核通过2已转款完成',
    pay_code    varchar(100)                               null comment '付款对账流水号',
    taxfee      decimal(10, 2)                default 0.00 null comment '手续费',
    error_code  varchar(100)                               null comment '转款失败错误代码'
)
    charset = utf8;

INSERT INTO bb2_seller.store_withdrawals (id, store_id, create_time, refuse_time, pay_time, check_time, money, bank_name, bank_card, realname, remark, status, pay_code, taxfee, error_code) VALUES (8, 123, 1600743343, 0, 0, 0, 29000.00, '工商银行', '643263745196549849846', '745745749549494949', '', 0, null, 0.00, null);