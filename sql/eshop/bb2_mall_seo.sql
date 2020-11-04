create table seo
(
    id          mediumint unsigned auto_increment comment '主键',
    title       varchar(255) not null comment '标题',
    keywords    varchar(255) not null comment '关键词',
    description text         not null comment '描述',
    type        varchar(20)  not null comment '类型',
    constraint id
        unique (id)
)
    comment 'SEO信息存放表' engine = MyISAM
                       charset = utf8;

