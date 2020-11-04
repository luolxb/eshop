create table user_message
(
    rec_id     int auto_increment
        primary key,
    user_id    mediumint unsigned  default 0 not null comment '用户id',
    message_id int(11) unsigned    default 0 not null comment '消息id',
    category   tinyint(1) unsigned default 0 not null comment '通知消息：0, 活动消息：1, 物流:2, 私信:3',
    is_see     tinyint(1) unsigned default 0 not null comment '是否查看：0未查看, 1已查看',
    deleted    tinyint(1)          default 0 not null comment '用户假删除标识,1:删除,0未删除',
    status     int                 default 0 not null comment '查看状态：0未查看，1已查看'
)
    charset = utf8;

create index message_id
    on user_message (message_id);

create index user_id
    on user_message (user_id);

INSERT INTO bb2_user.user_message (rec_id, user_id, message_id, category, is_see, deleted, status) VALUES (1, 335, 1, 0, 0, 0, 0);
INSERT INTO bb2_user.user_message (rec_id, user_id, message_id, category, is_see, deleted, status) VALUES (2, 336, 2, 0, 1, 1, 0);
INSERT INTO bb2_user.user_message (rec_id, user_id, message_id, category, is_see, deleted, status) VALUES (3, 337, 3, 0, 1, 0, 0);
INSERT INTO bb2_user.user_message (rec_id, user_id, message_id, category, is_see, deleted, status) VALUES (4, 338, 4, 0, 0, 0, 0);
INSERT INTO bb2_user.user_message (rec_id, user_id, message_id, category, is_see, deleted, status) VALUES (5, 339, 5, 0, 0, 0, 0);
INSERT INTO bb2_user.user_message (rec_id, user_id, message_id, category, is_see, deleted, status) VALUES (6, 340, 6, 0, 1, 0, 0);
INSERT INTO bb2_user.user_message (rec_id, user_id, message_id, category, is_see, deleted, status) VALUES (7, 341, 7, 0, 0, 0, 0);
INSERT INTO bb2_user.user_message (rec_id, user_id, message_id, category, is_see, deleted, status) VALUES (8, 342, 8, 0, 0, 0, 0);
INSERT INTO bb2_user.user_message (rec_id, user_id, message_id, category, is_see, deleted, status) VALUES (9, 343, 9, 0, 0, 0, 0);
INSERT INTO bb2_user.user_message (rec_id, user_id, message_id, category, is_see, deleted, status) VALUES (10, 344, 10, 0, 0, 0, 0);
INSERT INTO bb2_user.user_message (rec_id, user_id, message_id, category, is_see, deleted, status) VALUES (11, 345, 11, 0, 0, 0, 0);
INSERT INTO bb2_user.user_message (rec_id, user_id, message_id, category, is_see, deleted, status) VALUES (12, 346, 12, 0, 0, 0, 0);