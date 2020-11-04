create table feedback
(
    feedback_id int(10) auto_increment
        primary key,
    content     varchar(1400) null comment '反馈内容',
    pic1_url    varchar(64)   null comment '图片1',
    pic2_url    varchar(64)   null comment '图片2',
    pic3_url    varchar(64)   null comment '图片3',
    pic4_url    varchar(64)   null comment '图片4',
    user_id     int(10)       null comment '反馈人',
    create_time datetime      null comment '创建时间/反馈时间',
    reply       varchar(1400) null comment '回复',
    reply_time  datetime      null comment '回复时间'
);

INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (1, 'sdfsdfsdfsdfsfasdfsdfsdfsdfsdf', null, null, null, null, 331, '2020-08-27 15:44:33', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (2, 'sdfsfsdfsdfsdfsdfsdf', null, null, null, null, 331, '2020-08-27 15:46:24', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (3, 'sdfsfsdfsdfsdfsdfsdf', null, null, null, null, 331, '2020-08-27 15:47:22', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (4, 'fdfrhrhtrhr', null, null, null, null, 331, '2020-08-27 15:57:49', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (5, '寺', null, null, null, null, 331, '2020-08-27 16:04:21', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (6, '原封不动', null, null, null, null, 331, '2020-08-27 16:04:54', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (7, '标有', null, null, null, null, 331, '2020-08-27 16:07:54', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (8, '村', null, null, null, null, 331, '2020-08-27 16:10:39', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (9, '你明码标价', null, null, null, null, 331, '2020-08-27 16:52:29', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (10, '顶替', null, null, null, null, 331, '2020-08-27 17:08:05', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (11, '夺奇才地要枯无可奈何花落去 ', '', '', '', '', 331, '2020-08-27 18:36:02', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (12, '顶替在奇才', null, null, null, null, 331, '2020-08-27 18:38:16', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (13, '顶替', null, null, null, null, 331, '2020-08-27 18:39:11', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (14, '旧和 凸显', '', '', '', '', 331, '2020-08-27 18:41:13', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (15, '枯', '', '', '', '', 331, '2020-08-27 18:50:18', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (16, '盱顶替顶替顶替顶替枯', '', '', '', '', 331, '2020-08-27 18:52:17', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (17, '方闹得剖说说', '', '', '', '', 331, '2020-08-27 18:53:34', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (18, '34616514561', '', '', '', '', 331, '2020-08-27 18:54:26', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (19, '介有近qsjef你的及时雨粉为f', '', '', '', '', 331, '2020-08-28 08:59:47', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (20, 'gyjyjytjytjytjytjtyj', null, null, null, null, 331, '2020-08-28 09:00:49', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (21, '96389378387', null, null, null, null, 331, '2020-08-28 09:06:09', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (22, '36456464165641', null, null, null, null, 331, '2020-08-28 09:07:14', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (23, '454', null, null, null, null, 331, '2020-08-28 09:09:48', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (24, '4164161', null, null, null, null, 331, '2020-08-28 09:11:13', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (25, '64616', null, null, null, null, 331, '2020-08-28 09:13:47', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (26, '1656', null, null, null, null, 331, '2020-08-28 09:14:20', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (27, '6461611313', null, null, null, null, 336, '2020-08-28 15:44:33', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (28, '78678', null, null, null, null, 336, '2020-08-28 15:47:28', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (29, '64646', null, null, null, null, 336, '2020-08-28 15:50:11', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (30, '536543', null, null, null, null, 336, '2020-08-28 15:51:48', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (31, '5365436465', null, null, null, null, 336, '2020-08-28 15:53:13', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (32, '7987164', null, '/public/upload/head_pic/2020/8-28/1598601297049.jpg', null, null, 336, '2020-08-28 15:55:22', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (33, '6465464616', '/public/upload/head_pic/2020/8-28/1598609523613.jpg', '/public/upload/head_pic/2020/8-28/1598609520783.jpg', '/public/upload/head_pic/2020/8-28/1598609518980.jpg', null, 336, '2020-08-28 18:12:01', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (34, '464', '/public/upload/head_pic/2020/8-28/1598609615990.jpg', null, null, null, 336, '2020-08-28 18:13:26', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (35, 'Djdndbdhdhfhdjdjdjhdbdhd', null, null, null, '/public/upload/head_pic/2020/9-2/1599013477816.png', 336, '2020-09-02 10:24:35', null, null);
INSERT INTO bb2_mall.feedback (feedback_id, content, pic1_url, pic2_url, pic3_url, pic4_url, user_id, create_time, reply, reply_time) VALUES (36, '1023838839', null, null, null, '/public/upload/head_pic/2020/9-22/1600741952348.jpg', 373, '2020-09-22 10:32:24', null, null);