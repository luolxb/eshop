create table sms_log
(
    id         int auto_increment comment '表id'
        primary key,
    mobile     varchar(11) charset latin1  default '' null comment '手机号',
    session_id varchar(128) charset latin1 default '' null comment 'session_id',
    add_time   int                         default 0  null comment '发送时间',
    code       varchar(10) charset latin1  default '' null comment '验证码',
    seller_id  int(10)                     default 0  null,
    status     int(1)                      default 0  null comment '1:发送成功,0:发送失败',
    msg        varchar(255)                           null comment '短信内容',
    scene      int(1)                      default 0  null comment '发送场景,1:用户注册,2:找回密码,3:客户下单,4:客户支付,5:商家发货,6:身份验证',
    error_msg  text                                   null comment '发送短信异常内容'
)
    charset = utf8;

INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (258, '18687269789', '', 1600250390, '3909', 0, 1, null, 6, null);
INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (259, '13058140233', '', 1600250649, '2728', 0, 1, null, 6, null);
INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (260, '18923817688', '', 1600252747, '5755', 0, 1, null, 6, null);
INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (261, '18923817688', '', 1600252887, '4594', 0, 1, null, 6, null);
INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (262, '18923817688', '', 1600255185, '1468', 0, 1, null, 6, null);
INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (263, '18603059551', '', 1600256702, '7433', 0, 1, null, 6, null);
INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (264, '15889781738', '', 1600304067, '2027', 0, 1, null, 6, null);
INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (265, '15015102661', '', 1600397355, '3875', 0, 1, null, 6, null);
INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (266, '13066803937', '', 1600739593, '6792', 0, 1, null, 6, null);
INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (267, '18923817688', '', 1600761055, '5740', 0, 1, null, 2, null);
INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (268, '18923817688', '', 1600761775, '4712', 0, 1, null, 2, null);
INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (269, '18923817688', '', 1600762535, '2098', 0, 1, null, 2, null);
INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (270, '18923817688', '', 1600763607, '3115', 0, 1, null, 2, null);
INSERT INTO bb2_user.sms_log (id, mobile, session_id, add_time, code, seller_id, status, msg, scene, error_msg) VALUES (271, '13058140233', '', 1600849110, '1155', 0, 1, null, 2, null);