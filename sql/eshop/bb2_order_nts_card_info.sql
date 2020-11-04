create table nts_card_info
(
    id                   int auto_increment
        primary key,
    msisdn               varchar(32)          null,
    icc_id               varchar(32)          null,
    imsi                 varchar(32)          null,
    carrier              varchar(32)          null comment '运营商',
    sp_code              varchar(32)          null comment '短信端口号',
    data_plan            varchar(32)          null comment '套餐大小',
    expiry_date          date                 null comment '计费结束日期',
    data_usage           varchar(32)          null comment '当月流量',
    account_status       varchar(32)          null comment '卡状态',
    active               tinyint(1) default 0 null comment '激活/未激活  0:否  1:是',
    test_valid_date      date                 null comment '测试期起始日期',
    silent_valid_date    date                 null comment '沉默期起始日期',
    outbound_date        date                 null comment '出库日期',
    active_date          date                 null comment '激活日期',
    support_sms          tinyint(1)           null comment '是否支持短信  0:否  1:是',
    data_balance         varchar(32)          null comment '剩余流量',
    test_used_data_usage varchar(32)          null comment '测试期已用流量',
    create_time          datetime             null,
    update_time          datetime             null,
    remark               varchar(256)         null comment '备注',
    device_id            int                  null comment '设备id',
    company_id           int(10)              null comment '企业ID',
    user_id              int(10)              null comment '用户id'
)
    comment '物联卡信息表' charset = utf8;

