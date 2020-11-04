create table store_class
(
    sc_id   int unsigned auto_increment comment '索引ID'
        primary key,
    sc_name varchar(50)         default '' not null comment '分类名称',
    sc_bail mediumint unsigned  default 0  not null comment '保证金数额',
    sc_sort tinyint(1) unsigned default 0  not null comment '排序'
)
    comment '店铺分类表' charset = utf8;

INSERT INTO bb2_seller.store_class (sc_id, sc_name, sc_bail, sc_sort) VALUES (1, '女装/男装/内衣', 600, 99);
INSERT INTO bb2_seller.store_class (sc_id, sc_name, sc_bail, sc_sort) VALUES (2, '鞋靴/箱包/配件', 1000, 98);
INSERT INTO bb2_seller.store_class (sc_id, sc_name, sc_bail, sc_sort) VALUES (3, '家电/数码/手机', 1000, 97);
INSERT INTO bb2_seller.store_class (sc_id, sc_name, sc_bail, sc_sort) VALUES (4, '美妆/洗护/保健品', 1000, 96);
INSERT INTO bb2_seller.store_class (sc_id, sc_name, sc_bail, sc_sort) VALUES (5, '珠宝/眼镜/手表', 10000, 95);
INSERT INTO bb2_seller.store_class (sc_id, sc_name, sc_bail, sc_sort) VALUES (6, '美食/生鲜/零食', 6000, 92);
INSERT INTO bb2_seller.store_class (sc_id, sc_name, sc_bail, sc_sort) VALUES (7, '家具/家饰/家纺', 2000, 87);
INSERT INTO bb2_seller.store_class (sc_id, sc_name, sc_bail, sc_sort) VALUES (8, '运动/户外/乐器', 1000, 3);
INSERT INTO bb2_seller.store_class (sc_id, sc_name, sc_bail, sc_sort) VALUES (9, '图书/音像/电子书', 1000, 2);
INSERT INTO bb2_seller.store_class (sc_id, sc_name, sc_bail, sc_sort) VALUES (10, '虚拟产品', 1000, 1);
INSERT INTO bb2_seller.store_class (sc_id, sc_name, sc_bail, sc_sort) VALUES (11, '医药保健/计生情趣', 0, 255);