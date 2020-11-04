create table spec_type
(
    type_id int unsigned not null comment '类型id',
    spec_id int unsigned not null comment '规格id'
)
    comment '商品类型与规格对应表' charset = utf8;

INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (1, 1);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (1, 3);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (4, 7);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (4, 8);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (6, 9);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (6, 10);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (6, 11);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (7, 12);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (8, 15);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (8, 16);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (8, 17);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (8, 20);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (2, 4);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (2, 5);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (3, 6);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (3, 19);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (9, 1);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (9, 3);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (12, 7);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (12, 8);
INSERT INTO bb2_mall.spec_type (type_id, spec_id) VALUES (12, 22);