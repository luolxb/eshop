create table jobs_registry
(
    id          bigint auto_increment comment '主键 ID'
        primary key,
    app         varchar(100)         not null comment '服务名',
    address     varchar(100)         not null comment 'IP 地址',
    status      tinyint(2) default 0 not null comment '0、启用 1、已禁用',
    update_time bigint               not null comment '更新时间'
)
    comment '任务注册信息' charset = utf8;

INSERT INTO bb2_admin.jobs_registry (id, app, address, status, update_time) VALUES (9, 'jobs-user', '192.168.31.76:9997', 1, 1597134141973);
INSERT INTO bb2_admin.jobs_registry (id, app, address, status, update_time) VALUES (10, 'jobs-mall', '192.168.31.76:9999', 1, 1597134154301);
INSERT INTO bb2_admin.jobs_registry (id, app, address, status, update_time) VALUES (11, 'jobs-order', '192.168.31.76:9998', 1, 1597134153095);
INSERT INTO bb2_admin.jobs_registry (id, app, address, status, update_time) VALUES (12, 'jobs-user', '192.168.31.172:9997', 1, 1597318553488);
INSERT INTO bb2_admin.jobs_registry (id, app, address, status, update_time) VALUES (13, 'jobs-mall', '192.168.31.172:9999', 1, 1597318557154);
INSERT INTO bb2_admin.jobs_registry (id, app, address, status, update_time) VALUES (14, 'jobs-order', '192.168.31.172:9998', 1, 1597318550618);
INSERT INTO bb2_admin.jobs_registry (id, app, address, status, update_time) VALUES (15, 'jobs-mall', '192.168.31.174:9999', 1, 1597749734393);
INSERT INTO bb2_admin.jobs_registry (id, app, address, status, update_time) VALUES (16, 'jobs-user', '192.168.31.174:9997', 1, 1597749735461);
INSERT INTO bb2_admin.jobs_registry (id, app, address, status, update_time) VALUES (17, 'jobs-order', '192.168.31.174:9998', 1, 1597749734358);
INSERT INTO bb2_admin.jobs_registry (id, app, address, status, update_time) VALUES (18, 'jobs-order', '192.168.31.79:9998', 1, 1598866773252);
INSERT INTO bb2_admin.jobs_registry (id, app, address, status, update_time) VALUES (19, 'jobs-user', '192.168.31.79:9997', 1, 1598866766954);
INSERT INTO bb2_admin.jobs_registry (id, app, address, status, update_time) VALUES (20, 'jobs-mall', '192.168.31.79:9999', 1, 1598866763556);
INSERT INTO bb2_admin.jobs_registry (id, app, address, status, update_time) VALUES (21, 'jobs-user', '192.168.31.162:9997', 1, 1598512954395);