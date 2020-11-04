create table sms_template
(
    tpl_id       mediumint(8) auto_increment comment '自增ID'
        primary key,
    sms_sign     varchar(50)  default '' not null comment '短信签名',
    sms_tpl_code varchar(100) default '' not null comment '短信模板ID',
    tpl_content  varchar(512) default '' not null comment '发送短信内容',
    send_scene   varchar(100) default '' not null comment '短信发送场景',
    add_time     int                     not null comment '添加时间'
)
    comment '短信模板配置表' charset = utf8;

INSERT INTO bb2_user.sms_template (tpl_id, sms_sign, sms_tpl_code, tpl_content, send_scene, add_time) VALUES (1, '深圳市新时空智能系统验证', 'SMS_164620012', '验证码${code}，用户注册新账号, 请勿告诉他人，感谢您的支持!', '1', 1565676165);
INSERT INTO bb2_user.sms_template (tpl_id, sms_sign, sms_tpl_code, tpl_content, send_scene, add_time) VALUES (2, '深圳市新时空智能系统验证', 'SMS_199222153', '验证码${code}，用于密码找回，如非本人操作，请及时检查账户安全', '2', 1565676176);
INSERT INTO bb2_user.sms_template (tpl_id, sms_sign, sms_tpl_code, tpl_content, send_scene, add_time) VALUES (4, '新时空智能系统', 'SMS_199222157', '您有新订单，收货人：${consignee}，联系方式：${phone}，请您及时查收.', '3', 1565676185);
INSERT INTO bb2_user.sms_template (tpl_id, sms_sign, sms_tpl_code, tpl_content, send_scene, add_time) VALUES (5, '新时空智能系统', 'SMS_199202362', '尊敬的${userName}用户，您的订单已发货，收货人${consignee}，请您及时查收', '5', 1565676198);
INSERT INTO bb2_user.sms_template (tpl_id, sms_sign, sms_tpl_code, tpl_content, send_scene, add_time) VALUES (6, '新时空智能系统', 'SMS_199197220', '客户下的单(订单ID:${orderId})已经支付，请及时发货.', '4', 1565676204);
INSERT INTO bb2_user.sms_template (tpl_id, sms_sign, sms_tpl_code, tpl_content, send_scene, add_time) VALUES (7, '深圳市新时空智能系统验证', 'SMS_198585042', '尊敬的用户，您的验证码为${code}, 请勿告诉他人.', '6', 1565676209);