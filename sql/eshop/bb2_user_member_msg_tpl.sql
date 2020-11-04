create table member_msg_tpl
(
    mmt_code            varchar(50)      default '' not null comment '用户消息模板编号'
        primary key,
    mmt_name            varchar(50)      default '' not null comment '模板名称',
    mmt_message_switch  tinyint unsigned default 0  not null comment '站内信接收开关',
    mmt_message_content varchar(255)     default '' not null comment '站内信消息内容',
    mmt_short_switch    tinyint unsigned default 0  not null comment '短信接收开关',
    mmt_short_content   varchar(255)     default '' not null comment '短信接收内容',
    mmt_mail_switch     tinyint unsigned default 0  not null comment '邮件接收开关',
    mmt_mail_subject    varchar(255)     default '' not null comment '邮件标题',
    mmt_mail_content    text                        not null comment '邮件内容',
    mmt_short_sign      varchar(50)      default '' not null comment '短信签名',
    mmt_short_code      varchar(50)      default '' not null comment '短信模板ID'
)
    comment '用户消息模板' charset = utf8;

INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('coupon_get_notice', '优惠券到账提醒', 1, '您的优惠券到账啦，快去使用。', 0, '您的优惠券到账啦，快去使用。', 0, '1', '1', '12', '12');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('coupon_use_notice', '优惠券使用提醒', 1, '1', 0, '1', 0, '1', '1', '1', '1');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('coupon_will_expire_notice', '优惠券即将过期提醒', 1, '1', 0, '0', 0, '0', '0', '0', '0');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('deliver_goods_logistics', '发货提醒', 1, '0', 0, '0', 0, '0', '0', '0', '0');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('evaluate_logistics', '待评价提醒', 1, '0', 0, '0', 0, '0', '0', '0', '0');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('flash_sale_activity', '抢购活动', 1, '0', 0, '0', 0, '', '1', '00', '0');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('group_buy_activity', '团购', 1, '0', 0, '0', 0, '0', '0', '0', '0');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('message_notice', '系统通知', 1, '{$name}你好,店铺：<{$store_name}>悄悄的向你发了个消息。', 0, '1', 0, '多商家的消息，平台发', '<p>1这是多商家 测试的{$name}</p>', '【TPshop商城】', '123323');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('pay_cancel_logistics', '订单取消退款提醒', 1, '您的退款已到账，请查收。', 0, '0', 0, '0', '0', '0', '0');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('pre_sell_activity', '预售提醒', 1, '0', 0, '0', 0, '', '0', '0', '0');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('prom_goods_activity', '优惠促销', 1, '0', 0, '0', 0, '0', '0', '0', '0');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('prom_order_activity', '订单促销', 1, '0', 0, '0', 0, '0', '0', '0', '0');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('refund_logistics', '退款提醒', 1, '您的退款已到账，请查收。', 0, '0', 0, '0', '0', '0', '0');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('team_activity', '拼团活动', 1, '0', 0, '0', 0, '0', '0', '0', '0');
INSERT INTO bb2_user.member_msg_tpl (mmt_code, mmt_name, mmt_message_switch, mmt_message_content, mmt_short_switch, mmt_short_content, mmt_mail_switch, mmt_mail_subject, mmt_mail_content, mmt_short_sign, mmt_short_code) VALUES ('withdrawals_notice', '提现到账提醒', 1, '您申请提现已到账{$money}元。', 0, '0', 0, '0', '0', '0', '0');