create table brand_type
(
    type_id  int unsigned not null comment '类型id',
    brand_id int unsigned not null comment '品牌id'
)
    comment '商品类型与品牌对应表' charset = utf8;

INSERT INTO bb2_mall.brand_type (type_id, brand_id) VALUES (6, 11);
INSERT INTO bb2_mall.brand_type (type_id, brand_id) VALUES (8, 16);
INSERT INTO bb2_mall.brand_type (type_id, brand_id) VALUES (8, 17);
INSERT INTO bb2_mall.brand_type (type_id, brand_id) VALUES (8, 18);
INSERT INTO bb2_mall.brand_type (type_id, brand_id) VALUES (2, 9);
INSERT INTO bb2_mall.brand_type (type_id, brand_id) VALUES (3, 22);
INSERT INTO bb2_mall.brand_type (type_id, brand_id) VALUES (9, 14);
INSERT INTO bb2_mall.brand_type (type_id, brand_id) VALUES (9, 19);
INSERT INTO bb2_mall.brand_type (type_id, brand_id) VALUES (9, 20);