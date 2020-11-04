create table goods_type
(
    id   smallint unsigned auto_increment comment 'id自增'
        primary key,
    name varchar(60) default '' not null comment '类型名称'
)
    charset = utf8;

INSERT INTO bb2_mall.goods_type (id, name) VALUES (1, '手机');
INSERT INTO bb2_mall.goods_type (id, name) VALUES (2, '苹果6s');
INSERT INTO bb2_mall.goods_type (id, name) VALUES (3, '化妆品');
INSERT INTO bb2_mall.goods_type (id, name) VALUES (4, '电脑');
INSERT INTO bb2_mall.goods_type (id, name) VALUES (6, '4K高清');
INSERT INTO bb2_mall.goods_type (id, name) VALUES (7, '虚拟（会员视频）');
INSERT INTO bb2_mall.goods_type (id, name) VALUES (8, '情趣品');
INSERT INTO bb2_mall.goods_type (id, name) VALUES (9, '户外装备类');
INSERT INTO bb2_mall.goods_type (id, name) VALUES (11, '整店风格A');
INSERT INTO bb2_mall.goods_type (id, name) VALUES (12, '小清新');