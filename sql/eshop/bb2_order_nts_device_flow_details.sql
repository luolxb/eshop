create table nts_device_flow_details
(
    device_flow_details_id bigint auto_increment comment '编号'
        primary key,
    sort_id                varchar(64)  default '' null comment '排序ID',
    subscriber_id          varchar(100) default '' null comment '设备ID',
    carrier                varchar(100) default '' null comment '运营商',
    account                varchar(100) default '' null comment '账号ID',
    subaccount             varchar(100) default '' null comment '子账号ID',
    price_plan             varchar(100) default '' null comment '价格计划',
    sms_mo                 int                     null comment 'sms发送数据流量',
    sms_mt                 int                     null comment 'sms接受数据流量',
    data_mo                int                     null comment 'data发送数据流量',
    data_mt                int                     null comment 'data接受数据流量',
    data_both              int                     null comment 'data发送和接受数据流量',
    voice_mo               int                     null comment 'voice发送数据流量',
    voice_mt               int                     null comment 'voice接受数据流量',
    orbcomm_reports        int                     null comment 'orbcomm报告数据流量',
    orbcomm_messages       int                     null comment 'orbcomm消息数据流量',
    orbcomm_bytes          int                     null comment 'orbcomm数据流量',
    usage_time             datetime                null comment '使用时间',
    import_time            datetime                null comment '导入时间',
    statistics_time        datetime                null comment '统计时间',
    create_time            datetime                null comment '创建时间'
)
    comment '设备流量日志统计表';

