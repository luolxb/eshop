create table nts_device
(
    device_id        bigint auto_increment comment '编号'
        primary key,
    company_id       bigint                  null comment '企业ID',
    user_id          bigint                  null comment '用户ID',
    device_name      varchar(100) default '' null comment '设备名称',
    device_type_id   int(5)                  null comment '设备类型ID',
    device_desc      varchar(500) default '' null comment '设备描述',
    device_vendor_id int(5)                  null comment '设备制造商ID',
    activate         tinyint(1)   default 0  null comment '激活状态：0-未激活 1-已激活',
    activate_time    datetime                null comment '激活时间',
    device_state     tinyint(1)   default 0  null comment '设备状态：0-离线 1-在线 2-故障 3-欠费',
    device_sn        varchar(100) default '' null comment '设备SN',
    delete_flag      tinyint(1)   default 1  null comment '删除标志 1-正常状态 2-已删除',
    create_time      datetime                null comment '创建时间',
    update_time      datetime                null comment '更新时间',
    card_icc_id      varchar(32)             null comment '物联卡iccid'
)
    comment '设备表' collate = utf8mb4_bin;

