create table user_level
(
    level_id   smallint(4) unsigned auto_increment
        primary key,
    level_name varchar(30)           null comment '头衔名称',
    amount     decimal(10, 2)        null comment '等级必要金额',
    discount   smallint(4) default 0 null comment '折扣',
    `describe` varchar(200)          null
)
    charset = utf8;

INSERT INTO bb2_user.user_level (level_id, level_name, amount, discount, `describe`) VALUES (1, '注册会员', 500.00, 100, '');
INSERT INTO bb2_user.user_level (level_id, level_name, amount, discount, `describe`) VALUES (2, '铜牌会员', 1000.00, 100, '');
INSERT INTO bb2_user.user_level (level_id, level_name, amount, discount, `describe`) VALUES (3, '银牌会员', 5000.00, 100, '');
INSERT INTO bb2_user.user_level (level_id, level_name, amount, discount, `describe`) VALUES (4, '金牌会员', 10000.00, 100, '');
INSERT INTO bb2_user.user_level (level_id, level_name, amount, discount, `describe`) VALUES (5, '钻石会员', 50000.00, 100, '');