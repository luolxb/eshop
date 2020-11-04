create table nts_device_details
(
    device_details_id bigint auto_increment comment '编号'
        primary key,
    device_id         bigint                  null comment '设备ID',
    company_id        bigint                  null comment '企业ID',
    user_id           bigint                  null comment '用户ID',
    device_sn         varchar(100) default '' null comment '设备SN',
    carrier_id        int(5)                  null comment '服务商ID',
    account           varchar(100) default '' null comment '账号ID',
    subaccount        varchar(100) default '' null comment '子账号ID',
    price_plan        varchar(100) default '' null comment '价格计划',
    gateway_account   varchar(100) default '' null comment '网关账号',
    satellite_module  tinyint(1)   default 0  null comment '卫星模块：1-OGI 2-OG2',
    satellite_sn      varchar(100) default '' null comment '卫星模块SN',
    sim_operator      int(2)                  null comment 'SIM运营商：1-中国电信 2-中国移动 3-中国联通',
    sim_sn            varchar(100) default '' null comment 'SIM唯一SN',
    delete_flag       tinyint(1)   default 1  null comment '删除标志 1-正常状态 2-已删除',
    create_time       datetime                null comment '创建时间',
    update_time       datetime                null comment '更新时间',
    constraint device_id
        unique (device_id)
)
    comment '设备详情表' collate = utf8mb4_bin;

