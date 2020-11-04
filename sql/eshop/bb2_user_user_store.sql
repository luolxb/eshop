create table user_store
(
    id         mediumint unsigned auto_increment comment '表id'
        primary key,
    user_id    mediumint unsigned default 0  not null comment '用户id',
    store_name varchar(50)                   null comment '店铺名',
    true_name  varchar(50)                   null comment '真名',
    qq         varchar(20)        default '' not null comment 'QQ',
    mobile     varchar(20)        default '' not null comment '手机号码',
    store_img  varchar(255)                  null comment '店铺图片',
    store_time int                default 0  not null comment '开店时间'
)
    comment '用户店铺信息表' charset = utf8;

create index mobile
    on user_store (mobile);

create index user_id
    on user_store (user_id);

