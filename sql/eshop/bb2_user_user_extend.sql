create table user_extend
(
    id            int(11) unsigned auto_increment
        primary key,
    user_id       int(11) unsigned default 0  null,
    invoice_title varchar(200)                null comment '发票抬头',
    taxpayer      varchar(100)                null comment '纳税人识别号',
    invoice_desc  varchar(50)                 null comment '不开发票/明细',
    realname      varchar(100)                null comment '真实姓名',
    idcard        varchar(100)                null comment '身份证号',
    cash_alipay   varchar(100)     default '' not null comment '提现支付宝号',
    cash_unionpay varchar(100)     default '' not null comment '提现银行卡号'
)
    charset = utf8;

