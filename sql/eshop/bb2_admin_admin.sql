create table admin
(
    admin_id     smallint unsigned auto_increment comment '用户id'
        primary key,
    user_name    varchar(60)      default '' not null comment '用户名',
    email        varchar(60)      default '' not null,
    password     varchar(255)     default '' not null,
    add_time     int              default 0  not null,
    last_login   int              default 0  not null,
    last_ip      varchar(15)      default '' not null,
    nav_list     text                        null,
    lang_type    varchar(50)      default '' not null,
    paypwd       varchar(50)      default '' not null comment '支付密码',
    role_id      smallint(5)      default 0  null comment '角色id',
    suppliers_id smallint unsigned           null comment 'suppliers_id',
    site_id      int(11) unsigned default 0  not null comment '0没有城市分站，1有',
    open_teach   tinyint(1)       default 1  null
)
    charset = utf8;

create index agency_id
    on admin (paypwd);

create index user_name
    on admin (user_name);

INSERT INTO bb2_admin.admin (admin_id, user_name, email, password, add_time, last_login, last_ip, nav_list, lang_type, paypwd, role_id, suppliers_id, site_id, open_teach) VALUES (1, 'admin', 'wyp001@163.com', '{bcrypt}$2a$10$3Oc0pDSvB4leG2R/XzUQwOB4EdJAdd/3SY4WieiXXoyvAZjNrubyq', 1461978472, 1600305090, '172.24.0.1', '', '', '', 1, 6, 0, 1);
INSERT INTO bb2_admin.admin (admin_id, user_name, email, password, add_time, last_login, last_ip, nav_list, lang_type, paypwd, role_id, suppliers_id, site_id, open_teach) VALUES (2, 'shop', 'wyp001@163.com', '{bcrypt}$2a$10$zfttXeKuxIZu50s9A84GD.Y8dwZb2QxZlwUlg6prJrWOAR9T2x.um', 1461978472, 1588163219, '183.11.38.107', '', '', '', 1, 6, 0, 0);
INSERT INTO bb2_admin.admin (admin_id, user_name, email, password, add_time, last_login, last_ip, nav_list, lang_type, paypwd, role_id, suppliers_id, site_id, open_teach) VALUES (6, 'test', '1377327082@qq.com', '{bcrypt}$2a$10$YCE3gtsoP4KFVKCcNxnfRO3ZfepsBvNspUex5ORaR/nz936ecfoI6', 1521599192, 1522113946, '183.11.39.155', null, '', '', 5, 1, 0, 1);