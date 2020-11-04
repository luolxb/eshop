create table goods_collect
(
    collect_id mediumint unsigned auto_increment
        primary key,
    user_id    mediumint unsigned default 0 not null comment '用户id',
    goods_id   mediumint unsigned default 0 not null comment '商品id',
    cat_id3    mediumint(8)       default 0 not null,
    add_time   int(11) unsigned   default 0 not null comment '添加时间'
)
    charset = utf8;

create index goods_id
    on goods_collect (goods_id);

create index user_id
    on goods_collect (user_id);

INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (10, 336, 384, 0, 1598609412);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (12, 337, 388, 0, 1598840067);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (13, 340, 389, 0, 1598840556);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (14, 337, 389, 0, 1598923000);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (16, 337, 405, 0, 1599009796);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (17, 340, 405, 0, 1599010036);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (20, 337, 424, 0, 1599039270);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (21, 336, 403, 0, 1599041725);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (23, 340, 439, 0, 1599101571);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (24, 336, 439, 0, 1599101751);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (25, 340, 438, 0, 1599101914);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (26, 337, 443, 0, 1599201729);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (27, 340, 443, 0, 1599201838);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (30, 337, 404, 0, 1599705157);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (31, 355, 468, 0, 1600079870);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (32, 359, 473, 0, 1600239014);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (33, 359, 479, 0, 1600246120);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (34, 365, 483, 0, 1600252961);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (36, 364, 500, 0, 1600415382);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (37, 366, 501, 0, 1600416117);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (38, 364, 501, 0, 1600416099);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (39, 364, 502, 0, 1600416311);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (40, 364, 509, 0, 1600679658);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (41, 373, 517, 0, 1600739868);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (42, 363, 521, 0, 1600742370);
INSERT INTO bb2_mall.goods_collect (collect_id, user_id, goods_id, cat_id3, add_time) VALUES (43, 365, 514, 0, 1601283914);