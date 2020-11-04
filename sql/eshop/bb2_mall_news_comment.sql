create table news_comment
(
    comment_id     mediumint unsigned auto_increment
        primary key,
    article_id     smallint(5)           default 0  not null comment '新闻ID',
    user_id        mediumint unsigned    default 0  not null comment '用户ID',
    content        varchar(255)          default '' not null comment '评论内容',
    check_type     tinyint(255) unsigned default 0  null comment '审核状态 0未审核  1通过 2拒绝',
    check_describe varchar(255)                     null comment '审核理由描述',
    is_delete      tinyint(1) unsigned   default 0  not null comment '0正常显示，1虚拟删除不显示',
    add_time       int unsigned          default 0  not null,
    like_num       int(11) unsigned      default 0  not null comment '点赞量'
)
    engine = MyISAM
    charset = utf8;

create index article_id
    on news_comment (article_id);

create index user_id
    on news_comment (user_id);

