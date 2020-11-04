create table guarantee
(
    id         int auto_increment comment '自增ID'
        primary key,
    store_id   int                         not null comment '店铺ID',
    store_name varchar(500)                not null comment '店铺名称',
    grt_id     int                         not null comment '服务项目ID',
    joinstate  tinyint(1)     default 0    not null comment '加入状态 0未申请 1已申请 2已加入',
    cost       decimal(10, 2) default 0.00 not null comment '保证金余额',
    costimg    varchar(255)                null comment '付款凭证图',
    isopen     tinyint(1)     default 1    not null comment '0关闭 1开启',
    auditstate tinyint(1)     default 0    not null comment '申请审核状态0未审核1审核通过2审核失败3已支付保证金4保证金审核通过5保证金审核失败',
    quitstate  tinyint(1)     default 0    not null comment '退出申请状态0未申请 1已申请 2申请失败'
)
    comment '店铺消费者保障服务加入情况表' charset = utf8;

