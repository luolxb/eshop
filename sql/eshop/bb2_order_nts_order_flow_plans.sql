create table nts_order_flow_plans
(
    order_flow_plans_id bigint auto_increment comment '编号'
        primary key,
    order_id            bigint                       null comment '订单ID',
    order_type          tinyint(1)    default 0      null comment '订单类型 1-设备流量订单 2-企业流订单',
    plans_name          varchar(100)  default ''     null comment '套餐名称',
    plans_type          tinyint(1)    default 0      null comment '套餐类型 0-自定义流量 1-流量阶梯 2-按月缴费',
    plans_desc          varchar(100)  default ''     null comment '套餐描述',
    unit_price          decimal(20, 8)               null comment '流量单价 ?/kb',
    total_price         decimal(20, 8)               null comment '流量总价',
    flow_number         double(11, 4) default 0.0000 null comment '流量数量 单位：kb',
    months              int(4)        default 1      null comment '月收费：1月，3月，6月',
    create_time         datetime                     null comment '创建时间',
    update_time         datetime                     null comment '更新时间'
)
    comment '订单流量价格套餐表';

