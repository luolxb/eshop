create table message_activity
(
    message_id      int auto_increment
        primary key,
    message_title   varchar(255)                  not null comment '消息标题',
    message_content text                          null comment '消息内容',
    img_uri         varchar(255)                  null comment '图片地址',
    send_time       int unsigned                  not null comment '发送时间',
    end_time        int unsigned        default 0 not null comment '活动结束时间',
    mmt_code        varchar(50)                   not null comment '用户消息模板编号',
    prom_type       tinyint(1) unsigned default 0 not null comment '1抢购2团购3优惠促销4预售5虚拟6拼团7搭配购8自定义图文消息9订单促销',
    prom_id         int                 default 0 not null comment '活动id',
    store_id        int                 default 0 not null comment '店铺ID'
)
    charset = utf8;

