create table wx_menu
(
    id    int auto_increment
        primary key,
    name  varchar(50) default '' not null,
    type  varchar(20) default '' null comment '0 view 1 click',
    value varchar(255)           null,
    pid   int         default 0  null comment '上级菜单'
)
    charset = utf8;

