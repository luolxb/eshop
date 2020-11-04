create table message_private
(
    message_id      int auto_increment
        primary key,
    message_content text               not null comment '消息内容',
    send_time       int unsigned       not null comment '发送时间',
    send_user_id    mediumint unsigned not null comment '发送者'
)
    charset = utf8;

