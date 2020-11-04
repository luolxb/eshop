create table store_reopen
(
    re_id               int unsigned auto_increment,
    re_grade_id         smallint unsigned       default 0    not null comment '店铺等级ID',
    re_grade_name       varchar(30)                          null comment '等级名称',
    re_grade_price      decimal(10, 2) unsigned default 0.00 not null comment '等级收费(元/年)',
    re_year             tinyint unsigned        default 0    not null comment '续签时长(年)',
    re_pay_amount       decimal(10, 2) unsigned default 0.00 not null comment '应付总金额',
    re_store_name       varchar(50)                          null comment '店铺名字',
    re_store_id         int unsigned            default 0    not null comment '店铺ID',
    re_state            tinyint(3)              default 0    not null comment '状态0默认，未上传凭证1审核中2审核通过',
    re_start_time       int unsigned                         null comment '有效期开始时间',
    re_end_time         int unsigned                         null comment '有效期结束时间',
    re_create_time      int unsigned            default 0    not null comment '记录创建时间',
    re_pay_cert         varchar(150)                         null comment '付款凭证',
    re_pay_cert_explain varchar(300)                         null comment '付款凭证说明',
    admin_note          varchar(300)                         null comment '审核备注',
    primary key (re_id, re_create_time)
)
    comment '续签内容表' charset = utf8;

INSERT INTO bb2_seller.store_reopen (re_id, re_grade_id, re_grade_name, re_grade_price, re_year, re_pay_amount, re_store_name, re_store_id, re_state, re_start_time, re_end_time, re_create_time, re_pay_cert, re_pay_cert_explain, admin_note) VALUES (1, 4, '四金冠', 12000.00, 1, 12000.00, '喜斯原创', 4, -1, 1520329018, 1551865018, 1520329018, '/public/upload/store/4/reopen/53ff50f28a40da3c17bb2be08814777c.jpg', '已支付', '966666');
INSERT INTO bb2_seller.store_reopen (re_id, re_grade_id, re_grade_name, re_grade_price, re_year, re_pay_amount, re_store_name, re_store_id, re_state, re_start_time, re_end_time, re_create_time, re_pay_cert, re_pay_cert_explain, admin_note) VALUES (3, 3, '三皇冠', 8000.00, 2, 16000.00, '喜斯原创', 4, 2, 1521630833, 1584789233, 1521630833, '/public/upload/store/4/reopen/bdea9d05c38fe04ec6d2c0eeec8719a7.png', '78878888', '7777');
INSERT INTO bb2_seller.store_reopen (re_id, re_grade_id, re_grade_name, re_grade_price, re_year, re_pay_amount, re_store_name, re_store_id, re_state, re_start_time, re_end_time, re_create_time, re_pay_cert, re_pay_cert_explain, admin_note) VALUES (4, 3, '三皇冠', 8000.00, 1, 8000.00, '懂你', 37, -1, 1522379550, 1553915550, 1522379550, '/public/upload/store/37/reopen/9bf92c6ec168597a1a94d2023231374a.png', 'yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付yi已支付', '未收到付款');
INSERT INTO bb2_seller.store_reopen (re_id, re_grade_id, re_grade_name, re_grade_price, re_year, re_pay_amount, re_store_name, re_store_id, re_state, re_start_time, re_end_time, re_create_time, re_pay_cert, re_pay_cert_explain, admin_note) VALUES (5, 2, '二钻', 5000.00, 3, 15000.00, '懂你', 37, 2, 1522379684, 1617074084, 1522379684, '/public/upload/store/37/reopen/28289cef924a2dd4e62a8f73c0cefc1f.png', '失踪', '审核');