create table jobs_info
(
    id               bigint auto_increment comment '主键ID'
        primary key,
    tenant_id        varchar(100)         null comment '租户ID',
    app              varchar(100)         not null comment '服务名',
    cron             varchar(100)         not null comment '任务执行CRON',
    handler          varchar(255)         null comment '执行器任务handler',
    param            varchar(512)         null comment '执行器任务参数',
    timeout          int        default 0 not null comment '任务执行超时时间，单位秒',
    fail_retry_count int        default 0 not null comment '失败重试次数',
    last_time        bigint     default 0 not null comment '上次调度时间',
    next_time        bigint     default 0 not null comment '下次调度时间',
    author           varchar(30)          null comment '作者',
    remark           varchar(255)         null comment '备注',
    status           tinyint(2) default 0 not null comment '0、启用 1、已禁用',
    update_time      bigint               null comment '更新时间',
    create_time      bigint               not null comment '创建时间'
)
    comment '任务信息' charset = utf8;

INSERT INTO bb2_admin.jobs_info (id, tenant_id, app, cron, handler, param, timeout, fail_retry_count, last_time, next_time, author, remark, status, update_time, create_time) VALUES (1, null, 'jobs-mall', '0/1 * * * * ?', 'activityJobHandler', null, 30, 3, 1602573822204, 1602573823637, 'soubao', '每1秒执行一次', 0, null, 1592622709000);
INSERT INTO bb2_admin.jobs_info (id, tenant_id, app, cron, handler, param, timeout, fail_retry_count, last_time, next_time, author, remark, status, update_time, create_time) VALUES (2, null, 'jobs-mall', '0 0 1 1 * ?', 'mouthJobHandler', null, 30, 3, 1601514000000, 1604192400000, 'soubao', '每月1号凌晨1点执行一次', 0, null, 1592638707000);
INSERT INTO bb2_admin.jobs_info (id, tenant_id, app, cron, handler, param, timeout, fail_retry_count, last_time, next_time, author, remark, status, update_time, create_time) VALUES (3, null, 'jobs-order', '0 0 */1 * * ?', 'autoReceiveJob', null, 30, 3, 1598259600000, 1611478800000, 'soubao', '每小时执行一次,自动收货确认', 0, null, 1592638707000);
INSERT INTO bb2_admin.jobs_info (id, tenant_id, app, cron, handler, param, timeout, fail_retry_count, last_time, next_time, author, remark, status, update_time, create_time) VALUES (4, null, 'jobs-order', '0/1 * * * * ?', 'orderJobHandler', null, 30, 3, 1602573819545, 1602573822695, 'soubao', '每1秒执行一次', 0, null, 1592638707000);
INSERT INTO bb2_admin.jobs_info (id, tenant_id, app, cron, handler, param, timeout, fail_retry_count, last_time, next_time, author, remark, status, update_time, create_time) VALUES (5, null, 'jobs-order', '0 0 0 1-2 * ?', 'autoConfirmJob', null, 30, 3, 1601596800000, 1604188800000, 'soubao', '每天执行一次,分销记录自动分成', 0, null, 1592638707000);
INSERT INTO bb2_admin.jobs_info (id, tenant_id, app, cron, handler, param, timeout, fail_retry_count, last_time, next_time, author, remark, status, update_time, create_time) VALUES (6, null, 'jobs-user', '0 0 1 1 * ?', 'mouthJobHandler', null, 30, 3, 1601514000000, 1604192400000, 'soubao', '删除一个月以前的短信', 0, null, 1592793884000);