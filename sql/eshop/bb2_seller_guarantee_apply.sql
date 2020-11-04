create table guarantee_apply
(
    id         int auto_increment comment '申请ID'
        primary key,
    grt_id     int                         not null comment '保障项目ID',
    store_id   int                         not null comment '店铺ID',
    store_name varchar(500)                not null comment '店铺名称',
    add_time   int                         not null comment '申请时间',
    auditstate tinyint(1)     default 0    not null comment '审核状态 0未审核 1审核通过 2审核失败 3保证金待审核 4保证金审核通过 5保证金审核失败',
    cost       decimal(10, 2) default 0.00 null comment '保证金金额',
    costimg    varchar(500)                null comment '保证金付款凭证图片',
    apply_type tinyint(1)     default 1    null comment '申请类型0退出1加入'
)
    comment '消费者保障服务申请表' charset = utf8;

INSERT INTO bb2_seller.guarantee_apply (id, grt_id, store_id, store_name, add_time, auditstate, cost, costimg, apply_type) VALUES (1, 2, 44, '华莱士', 1562309306, 0, 50.00, null, 1);