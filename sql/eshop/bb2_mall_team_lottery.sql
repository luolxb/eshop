create table team_lottery
(
    id       int(11) unsigned auto_increment
        primary key,
    user_id  int          default 0  null comment '幸运儿手机',
    order_id int          default 0  null comment '订单id',
    order_sn varchar(50)             null,
    mobile   varchar(20)  default '' null comment '幸运儿手机',
    team_id  int          default 0  null comment '拼团活动ID',
    nickname varchar(100) default '' null comment '会员昵称',
    head_pic varchar(150) default '' null comment '幸运儿头像'
)
    charset = utf8;

