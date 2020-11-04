create table nts_device_flow_pool
(
    flow_pool_id bigint auto_increment comment '编号'
        primary key,
    device_id    bigint                  null comment '设备ID',
    device_sn    varchar(100) default '' null comment '设备SN',
    surplus_flow double(20, 4)           null comment '剩余流量',
    used_flow    double(20, 4)           null comment '已用流量',
    delete_flag  tinyint(1)   default 1  null comment '删除标志 1-正常状态 2-已删除',
    create_time  datetime                null comment '创建时间',
    update_time  datetime                null comment '更新时间',
    constraint device_id
        unique (device_id)
)
    comment '设备流量池表' collate = utf8mb4_bin;

