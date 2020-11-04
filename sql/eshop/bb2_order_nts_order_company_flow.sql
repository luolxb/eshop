create table nts_order_company_flow
(
    order_company_flow_id bigint auto_increment comment '编号'
        primary key,
    company_id            bigint                  null comment '企业ID',
    create_user_id        bigint                  null comment '创建者ID',
    update_user_id        bigint                  null comment '更新者ID',
    order_code            varchar(64)  default '' null comment '订单编号',
    order_status          tinyint(1)   default 0  null comment '订单状态 0-待付款 1-处理中 2-已完成 3-取消 4-售后',
    flow_price            decimal(20, 8)          null comment '流量总价',
    order_price           decimal(20, 8)          null comment '订单总价',
    actual_price          decimal(20, 8)          null comment '实际需要支付的金额',
    currency_type_id      int(5)                  null comment '货币类别ID',
    pay_model_id          int(5)                  null comment '支付方式ID',
    pay_name              varchar(100) default '' null comment '支付名称',
    pay_code              varchar(100) default '' null comment '支付流水号',
    pay_status            tinyint(1)   default 0  null comment '支付状态 0-初始化 1-付款中,2-支付成功',
    payer                 varchar(120) default '' null comment '支付人',
    pay_callback          tinyint(1)   default 0  null comment '支付回调状态 0-初始化 1-成功,-1-失败',
    pay_time              datetime                null comment '支付时间',
    remark                varchar(500) default '' null comment '备注',
    delete_flag           tinyint(1)   default 1  null comment '删除标志 1-正常状态 2-已删除',
    create_time           datetime                null comment '创建时间',
    update_time           datetime                null comment '更新时间'
)
    comment '企业流量订单表';

create index _order_company_flow_company
    on nts_order_company_flow (company_id);

