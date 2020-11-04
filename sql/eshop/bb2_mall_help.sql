create table help
(
    help_id    int unsigned auto_increment comment '帮助ID'
        primary key,
    help_sort  tinyint(1) unsigned default 255 null comment '排序',
    help_title varchar(100)                    not null comment '标题',
    help_info  text                            null comment '帮助内容',
    help_url   varchar(100)        default ''  null comment '跳转链接',
    add_time   int unsigned                    not null comment '更新时间',
    type_id    int unsigned                    not null comment '帮助类型',
    page_show  tinyint(1) unsigned default 1   null comment '页面类型:1为店铺,2为会员,默认为1',
    is_show    tinyint(1)          default 1   null comment '是否显示',
    keywords   varchar(255)                    null comment '关键字'
)
    comment '帮助内容表' engine = MyISAM
                    charset = utf8;

