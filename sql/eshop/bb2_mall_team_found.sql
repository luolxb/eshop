create table team_found
(
    found_id       int unsigned auto_increment
        primary key,
    found_time     int(11) unsigned        default 0    not null comment '开团时间',
    found_end_time int(11) unsigned        default 0    not null comment '成团截止时间',
    user_id        int(11) unsigned        default 0    not null comment '团长id',
    team_id        int unsigned            default 0    not null comment '拼团活动id',
    nickname       varchar(100)            default ''   not null comment '团长用户名昵称',
    head_pic       varchar(255)            default ''   not null comment '团长头像',
    order_sn       varchar(20)             default ''   not null comment '订单编号',
    order_id       int(11) unsigned        default 0    not null comment '团长订单id',
    `join`         int(8) unsigned         default 1    not null comment '已参团人数',
    need           int(8) unsigned         default 1    not null comment '需多少人成团',
    price          decimal(10, 2) unsigned default 0.00 not null comment '拼团价格',
    goods_price    decimal(10, 2) unsigned default 0.00 not null comment '商品原价',
    status         tinyint(1) unsigned     default 0    not null comment '拼团状态0:待开团(表示已下单但是未支付)1:已经开团(团长已支付)2:拼团成功,3拼团失败',
    bonus_status   tinyint(1) unsigned     default 0    not null comment '团长佣金领取状态：0无1领取',
    store_id       int(11) unsigned        default 0    not null comment '店铺id',
    is_auto        tinyint(1) unsigned     default 0    not null comment '是否已自动处理：0无1是'
)
    comment '开团表' charset = utf8;

