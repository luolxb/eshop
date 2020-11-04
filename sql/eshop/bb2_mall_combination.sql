create table combination
(
    combination_id int unsigned auto_increment comment '主键'
        primary key,
    title          varchar(255)        default '' not null comment '标题',
    `desc`         varchar(255)        default '' not null comment '描述',
    is_on_sale     tinyint(1) unsigned default 0  not null comment '上下架，0下，1上',
    start_time     int unsigned        default 0  not null comment '活动有效起始时间',
    end_time       int unsigned        default 0  not null comment '活动有效截止时间',
    status         tinyint(4) unsigned default 0  not null comment '审核状态,0待审核;1审核通过;2审核拒绝',
    store_id       int unsigned        default 0  not null comment '店铺id',
    is_deleted     tinyint(4) unsigned default 0  not null comment '是否删除'
)
    comment '组合促销表' engine = MyISAM
                    charset = utf8;

