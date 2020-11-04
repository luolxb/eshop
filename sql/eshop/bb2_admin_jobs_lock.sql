create table jobs_lock
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    name        varchar(30)  not null comment '名称',
    owner       varchar(100) not null comment '持有者',
    create_time bigint       not null comment '创建时间',
    constraint uidx_name
        unique (name)
)
    comment '任务锁' charset = utf8;

INSERT INTO bb2_admin.jobs_lock (id, name, owner, create_time) VALUES (3741202, 'JOBS_LOCK', '4df7eb7b-2833-4ba2-99e9-d96ab804e27f', 1602573822553);