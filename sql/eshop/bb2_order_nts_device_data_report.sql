create table nts_device_data_report
(
    device_data_report_id bigint auto_increment comment '编号'
        primary key,
    account               varchar(100) default '' null comment '账号',
    organization          varchar(100) default '' null comment '组织',
    time_zone             varchar(100) default '' null comment '时区',
    device_sn             varchar(100) default '' null comment '设备SN',
    device_event          varchar(100) default '' null comment '设备事件',
    device_time           datetime                null comment '设备时间',
    device_time_local     datetime                null comment '设备本地时间',
    device_type           varchar(64)  default '' null comment '设备类型',
    device_speed          varchar(40)  default '' null comment '设备速度',
    gps_address           varchar(200) default '' null comment 'GPS地址',
    gps_mode              varchar(100) default '' null comment 'GPS模式',
    latitude              varchar(64)  default '' null comment 'GPS维度',
    longitude             varchar(64)  default '' null comment 'GPS经度',
    satellites_used       varchar(40)  default '' null comment '是否使用卫星',
    temperature           varchar(40)  default '' null comment '温度',
    battery_voltage       varchar(40)  default '' null comment '电池电压',
    firmware_version      varchar(100) default '' null comment '固件版本',
    read_id               varchar(60)  default '' null comment '读取ID',
    create_time           datetime                null comment '创建时间',
    constraint read_id
        unique (read_id)
)
    comment '设备数据上报表' collate = utf8mb4_bin;

