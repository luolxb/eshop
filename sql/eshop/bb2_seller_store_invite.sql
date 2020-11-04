create table store_invite
(
    id             int(11) unsigned auto_increment
        primary key,
    user_id        int(11) unsigned default 0 not null,
    invite_user_id int(11) unsigned default 0 not null comment '谁邀请开店铺',
    add_time       int(11) unsigned default 0 not null,
    update_time    int(11) unsigned default 0 not null
)
    engine = MyISAM
    charset = utf8;

