create table invoice
(
    invoice_id    int(11) unsigned auto_increment
        primary key,
    order_id      int(20) unsigned          default 0      not null comment '订单id',
    order_sn      varchar(20) charset utf8  default ''     not null comment '订单编号',
    user_id       int(11) unsigned          default 0      not null comment '用户id',
    store_id      int(11) unsigned          default 0      not null comment '商家id',
    invoice_type  tinyint(1)                default 0      not null comment '0普通发票1电子发票2增值税发票',
    invoice_money decimal(10, 2) unsigned   default 0.00   not null comment '发票金额',
    invoice_title varchar(255) charset utf8 default ''     not null comment '发票抬头',
    invoice_desc  varchar(255) charset utf8 default ''     not null comment '发票内容',
    invoice_rate  decimal(10, 4) unsigned   default 0.0000 not null comment '发票税率',
    taxpayer      varchar(50) charset utf8  default ''     not null comment '纳税人识别号',
    status        tinyint(1)                default 0      not null comment '发票状态0待开1已开2作废',
    atime         int                       default 0      not null comment '开票时间',
    ctime         int(11) unsigned          default 0      not null comment '创建时间'
)
    comment '发票信息表' charset = latin1;

