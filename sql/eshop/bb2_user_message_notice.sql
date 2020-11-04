create table message_notice
(
    message_id      int auto_increment
        primary key,
    message_type    tinyint(1) unsigned default 0 not null comment '个体消息：0，全体消息:1',
    message_title   varchar(255)                  null comment '消息标题',
    message_content text                          not null comment '消息内容',
    send_time       int unsigned                  not null comment '发送时间',
    mmt_code        varchar(50)                   null comment '用户消息模板编号',
    type            tinyint(1) unsigned default 0 null comment '0系统公告1降价通知2优惠券到账提醒3优惠券使用提醒4优惠券即将过期提醒5预售订单尾款支付提醒6提现到账提醒',
    prom_id         int                 default 0 not null comment '活动id',
    store_id        int                 default 0 null comment '店铺ID'
)
    charset = utf8;

INSERT INTO bb2_user.message_notice (message_id, message_type, message_title, message_content, send_time, mmt_code, type, prom_id, store_id) VALUES (1, 0, '信息通知', '13040580895你好,店铺：<{$store_name}>悄悄的向你发了个消息。', 1599013784, 'message_notice', 0, 0, 0);
INSERT INTO bb2_user.message_notice (message_id, message_type, message_title, message_content, send_time, mmt_code, type, prom_id, store_id) VALUES (2, 0, '信息通知', '18923817688你好,店铺：<{$store_name}>悄悄的向你发了个消息。', 1599013784, 'message_notice', 0, 0, 0);
INSERT INTO bb2_user.message_notice (message_id, message_type, message_title, message_content, send_time, mmt_code, type, prom_id, store_id) VALUES (3, 0, '信息通知', '浅安你好,店铺：<{$store_name}>悄悄的向你发了个消息。', 1599013784, 'message_notice', 0, 0, 0);
INSERT INTO bb2_user.message_notice (message_id, message_type, message_title, message_content, send_time, mmt_code, type, prom_id, store_id) VALUES (4, 0, '信息通知', '18603059551你好,店铺：<{$store_name}>悄悄的向你发了个消息。', 1599013784, 'message_notice', 0, 0, 0);
INSERT INTO bb2_user.message_notice (message_id, message_type, message_title, message_content, send_time, mmt_code, type, prom_id, store_id) VALUES (5, 0, '信息通知', '13052417485你好,店铺：<{$store_name}>悄悄的向你发了个消息。', 1599013784, 'message_notice', 0, 0, 0);
INSERT INTO bb2_user.message_notice (message_id, message_type, message_title, message_content, send_time, mmt_code, type, prom_id, store_id) VALUES (6, 0, '信息通知', '18687269789你好,店铺：<{$store_name}>悄悄的向你发了个消息。', 1599013784, 'message_notice', 0, 0, 0);
INSERT INTO bb2_user.message_notice (message_id, message_type, message_title, message_content, send_time, mmt_code, type, prom_id, store_id) VALUES (7, 0, '信息通知', '13300101011你好,店铺：<{$store_name}>悄悄的向你发了个消息。', 1599013784, 'message_notice', 0, 0, 0);
INSERT INTO bb2_user.message_notice (message_id, message_type, message_title, message_content, send_time, mmt_code, type, prom_id, store_id) VALUES (8, 0, '信息通知', '18603053050你好,店铺：<{$store_name}>悄悄的向你发了个消息。', 1599013784, 'message_notice', 0, 0, 0);
INSERT INTO bb2_user.message_notice (message_id, message_type, message_title, message_content, send_time, mmt_code, type, prom_id, store_id) VALUES (9, 0, '信息通知', '18587569585你好,店铺：<{$store_name}>悄悄的向你发了个消息。', 1599013784, 'message_notice', 0, 0, 0);
INSERT INTO bb2_user.message_notice (message_id, message_type, message_title, message_content, send_time, mmt_code, type, prom_id, store_id) VALUES (10, 0, '信息通知', '13066803937你好,店铺：<{$store_name}>悄悄的向你发了个消息。', 1599013784, 'message_notice', 0, 0, 0);
INSERT INTO bb2_user.message_notice (message_id, message_type, message_title, message_content, send_time, mmt_code, type, prom_id, store_id) VALUES (11, 0, '信息通知', '18687532589你好,店铺：<{$store_name}>悄悄的向你发了个消息。', 1599013784, 'message_notice', 0, 0, 0);
INSERT INTO bb2_user.message_notice (message_id, message_type, message_title, message_content, send_time, mmt_code, type, prom_id, store_id) VALUES (12, 0, '信息通知', '13052859681你好,店铺：<{$store_name}>悄悄的向你发了个消息。', 1599013784, 'message_notice', 0, 0, 0);