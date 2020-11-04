create table guarantee_costlog
(
    id         int auto_increment comment '自增ID'
        primary key,
    grt_id     int            not null comment '保障项目ID',
    grt_name   varchar(100)   not null comment '保障项目名称',
    store_id   int            not null comment '店铺ID',
    store_name varchar(500)   not null comment '店铺名称',
    admin_id   int            null comment '操作管理员ID',
    admin_name varchar(200)   null comment '操作管理员名称',
    price      decimal(10, 2) not null comment '金额',
    add_time   int            not null comment '添加时间',
    `desc`     varchar(2000)  not null comment '描述'
)
    comment '店铺消费者保障服务保证金日志表' charset = utf8;

