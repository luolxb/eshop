create table message
(
    message_id int(11) unsigned auto_increment comment '消息ID'
        primary key,
    admin_id   smallint unsigned   default 0 not null comment '管理员id',
    seller_id  smallint unsigned   default 0 not null comment '商家管理员id',
    message    text                          not null comment '站内信内容',
    type       tinyint(1) unsigned default 0 not null comment '个体消息：0，全体消息：1',
    category   tinyint(1) unsigned default 0 not null comment '0系统消息，1物流通知，2优惠促销，3商品提醒，4我的资产，5商城好店',
    send_time  int unsigned                  not null comment '发送时间',
    data       text                          not null comment '消息序列化内容'
)
    charset = utf8;

