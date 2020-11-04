create table message_logistics
(
    message_id      int auto_increment
        primary key,
    message_title   varchar(255)                   null comment '消息标题',
    message_content text                           not null comment '消息内容',
    img_uri         varchar(255)                   null comment '图片地址',
    send_time       int unsigned                   not null comment '发送时间',
    order_sn        varchar(20)         default '' not null comment '单号',
    order_id        int                 default 0  not null comment '物流订单id',
    mmt_code        varchar(50)                    null comment '用户消息模板编号',
    type            tinyint(1) unsigned default 0  null comment '1到货通知2发货提醒3签收提醒4评价提醒5退货提醒6退款提醒',
    store_id        int                 default 0  not null comment '店铺ID'
)
    charset = utf8;

