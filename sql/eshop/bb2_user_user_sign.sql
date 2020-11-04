create table user_sign
(
    id         int(11) unsigned auto_increment
        primary key,
    user_id    int      default 0   null comment '用户id',
    sign_total int      default 0   null comment '累计签到天数',
    sign_count int      default 0   null comment '连续签到天数',
    sign_last  char(11) default '0' null comment '最后签到时间，时间格式20170907',
    sign_time  text charset utf8    null comment '历史签到时间，以逗号隔开',
    cumtrapz   int      default 0   null comment '用户累计签到总积分',
    this_month int(6)               null comment '本月累计积分'
)
    charset = latin1;

