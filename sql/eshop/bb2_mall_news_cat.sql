create table news_cat
(
    cat_id      int unsigned auto_increment
        primary key,
    cat_name    varchar(20)           null comment '类别名称',
    cat_type    smallint   default 0  null comment '默认分组',
    parent_id   smallint   default 0  null comment '夫级ID',
    show_in_nav tinyint(1) default 0  null comment '是否导航显示',
    sort_order  smallint   default 50 null comment '排序',
    cat_desc    varchar(255)          null comment '分类描述',
    keywords    varchar(30)           null comment '搜索关键词',
    cat_alias   varchar(20)           null comment '别名'
)
    charset = utf8;

