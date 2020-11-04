create table prom_order
(
    id          bigint auto_increment
        primary key,
    title       varchar(60)           default ''   not null comment '活动名称',
    type        int(2)                default 0    not null comment '活动类型 0满额打折,1满额优惠金额,2满额送积分,3满额送优惠券',
    money       float(10, 2) unsigned default 0.00 not null comment '最小金额',
    expression  varchar(100)                       not null comment '优惠体现',
    description text                               not null comment '活动描述',
    start_time  int(11) unsigned                   not null comment '活动开始时间',
    end_time    int(11) unsigned                   not null comment '活动结束时间',
    status      tinyint(1) unsigned   default 1    not null comment '1正常，0管理员关闭',
    `group`     varchar(255)          default ''   null comment '适用范围',
    prom_img    varchar(255)                       not null comment '活动宣传图',
    store_id    int(11) unsigned      default 0    not null comment '商家店铺id',
    orderby     int unsigned          default 0    not null comment '排序',
    recommend   tinyint(4) unsigned   default 0    not null comment '是否推荐',
    is_deleted  tinyint(1) unsigned   default 0    not null comment '是否删除1为是,0为否'
)
    charset = utf8;

