create table store_msg_tpl
(
    smt_code            varchar(100)           not null comment '模板编码'
        primary key,
    smt_name            varchar(100)           not null comment '模板名称',
    smt_message_switch  tinyint unsigned       not null comment '站内信默认开关，0关，1开',
    smt_message_content varchar(255)           not null comment '站内信内容',
    smt_message_forced  tinyint unsigned       not null comment '站内信强制接收，0否，1是',
    smt_short_switch    tinyint unsigned       not null comment '短信默认开关，0关，1开',
    smt_short_content   varchar(255)           not null comment '短信内容',
    smt_short_forced    tinyint unsigned       not null comment '短信强制接收，0否，1是',
    smt_mail_switch     tinyint unsigned       not null comment '邮件默认开，0关，1开',
    smt_mail_subject    varchar(255)           not null comment '邮件标题',
    smt_mail_content    text                   not null comment '邮件内容',
    smt_mail_forced     tinyint unsigned       not null comment '邮件强制接收，0否，1是',
    smt_short_sign      varchar(50) default '' not null comment '短信签名',
    smt_short_code      varchar(50) default '' not null comment '短信模板ID'
)
    comment '商家消息模板' engine = MyISAM
                     charset = utf8;

