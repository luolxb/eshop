create table news
(
    article_id     mediumint unsigned auto_increment
        primary key,
    cat_id         smallint(5)         default 0  not null comment '类别ID',
    title          varchar(150)        default '' not null comment '文章标题',
    check_type     tinyint(1) unsigned default 1  not null comment '审核状态 0未审核  1通过 2拒绝',
    check_describe mediumtext                     null comment '审核理由描述',
    tags           char(64)                       null comment '新闻标签',
    content        longtext                       not null,
    user_id        int(11) unsigned    default 0  not null comment '作者id（用户id）',
    author         varchar(30)         default '' not null comment '文章作者',
    author_email   varchar(60)         default '' not null comment '作者邮箱',
    keywords       varchar(255)        default '' not null comment '关键字',
    article_type   tinyint(1) unsigned default 2  not null,
    is_open        tinyint(1) unsigned default 1  not null,
    add_time       int unsigned        default 0  not null,
    file_url       varchar(255)        default '' not null comment '附件地址',
    open_type      tinyint(1) unsigned default 0  not null,
    link           varchar(255)        default '' not null comment '链接地址',
    description    mediumtext                     null comment '文章摘要',
    click          int                 default 0  null comment '浏览量',
    publish_time   int                            null comment '文章预告发布时间',
    thumb          varchar(255)        default '' null comment '文章缩略图',
    thumb2         varchar(255)        default '' null comment '文章缩略图2',
    thumb3         varchar(255)        default '' null comment '文章缩略图3',
    pickup_id      int(8) unsigned                null comment '新闻来源  发布新闻的管理员角色id',
    type           tinyint(1) unsigned default 1  null comment '新闻列表显示样式 0不显示图片 1左显示，2右显示 3上显示 4下显示',
    source         tinyint(1) unsigned default 0  not null comment '新闻来源 、0=总平台 1=商家 2=用户'
)
    charset = utf8;

create index cat_id
    on news (cat_id);

