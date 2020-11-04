create table jobs_log
(
    id               bigint auto_increment comment '主键ID'
        primary key,
    job_id           bigint        not null comment '任务ID',
    address          varchar(255)  null comment '执行地址',
    handler          varchar(255)  not null comment '任务 handler',
    param            varchar(512)  null comment '任务参数',
    fail_retry_count int default 0 not null comment '失败重试次数',
    trigger_code     int default 0 not null comment '触发器调度返回码',
    trigger_type     varchar(30)   not null comment '触发器调度类型',
    trigger_msg      text          null comment '触发器调度返回信息',
    create_time      bigint        not null comment '创建时间'
)
    comment '任务调度日志' charset = utf8;

