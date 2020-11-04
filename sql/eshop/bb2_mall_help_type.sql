create table help_type
(
    type_id    int unsigned auto_increment comment '类型ID'
        primary key,
    type_name  varchar(50)                        not null comment '类型名称',
    sort_order tinyint(1) unsigned default 255    null comment '排序',
    help_code  varchar(10)         default 'auto' null comment '调用编号(auto的可删除)',
    is_show    tinyint(1) unsigned default 1      null comment '是否显示,0为否,1为是,默认为1',
    help_show  tinyint(1) unsigned default 1      null comment '页面类型:1为店铺,2为会员,默认为1',
    pid        int(8)              default 0      null comment '默认0为一级分类',
    level      tinyint(2)          default 0      null
)
    comment '帮助类型表' charset = utf8;

