create table oauth_users
(
    tu_id       mediumint unsigned auto_increment comment '表自增ID'
        primary key,
    user_id     mediumint(8) not null comment '用户表ID',
    openid      varchar(255) not null comment '第三方开放平台openid',
    oauth       varchar(50)  not null comment '第三方授权平台',
    unionid     varchar(255) null,
    oauth_child varchar(50)  null comment 'mp标识来自公众号, open标识来自开放平台,用于标识来自哪个第三方授权平台, 因为同是微信平台有来自公众号和开放平台'
)
    charset = utf8;

