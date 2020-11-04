create table team_follow
(
    follow_id            int(11) unsigned auto_increment
        primary key,
    follow_user_id       int(11) unsigned    default 0  not null comment '参团会员id',
    follow_user_nickname varchar(100)        default '' not null comment '参团会员昵称',
    follow_user_head_pic varchar(255)        default '' not null comment '会员头像',
    follow_time          int(11) unsigned    default 0  not null comment '参团时间',
    order_sn             varchar(20)         default '' not null comment '订单编号',
    order_id             int(11) unsigned    default 0  not null comment '订单id',
    found_id             int unsigned        default 0  not null comment '开团ID',
    found_user_id        int(11) unsigned    default 0  not null comment '开团人user_id',
    team_id              int unsigned        default 0  not null comment '拼团活动id',
    status               tinyint(1) unsigned default 0  not null comment '参团状态0:待拼单(表示已下单但是未支付)1拼单成功(已支付)2成团成功3成团失败',
    is_win               tinyint(1) unsigned default 0  not null comment '抽奖团是否中奖'
)
    comment '参团表' charset = utf8;

