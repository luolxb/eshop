create table gen_table
(
    table_id        bigint auto_increment comment '编号'
        primary key,
    table_name      varchar(200) default ''     null comment '表名称',
    table_comment   varchar(500) default ''     null comment '表描述',
    class_name      varchar(100) default ''     null comment '实体类名称',
    tpl_category    varchar(200) default 'crud' null comment '使用的模板（crud单表操作 tree树表操作）',
    package_name    varchar(100)                null comment '生成包路径',
    module_name     varchar(30)                 null comment '生成模块名',
    business_name   varchar(30)                 null comment '生成业务名',
    function_name   varchar(50)                 null comment '生成功能名',
    function_author varchar(50)                 null comment '生成功能作者',
    options         varchar(1000)               null comment '其它生成选项',
    create_by       varchar(64)  default ''     null comment '创建者',
    create_time     datetime                    null comment '创建时间',
    update_by       varchar(64)  default ''     null comment '更新者',
    update_time     datetime                    null comment '更新时间',
    remark          varchar(500)                null comment '备注'
)
    comment '代码生成业务表';

