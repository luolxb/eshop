create table store_extend
(
    store_id        mediumint unsigned default 0 not null comment '店铺ID'
        primary key,
    express         text                         null comment '快递公司ID的组合',
    pricerange      text                         null comment '店铺统计设置的商品价格区间',
    orderpricerange text                         null comment '店铺统计设置的订单价格区间',
    pic_num         int(10)            default 0 null comment '已上传图片数量'
)
    charset = utf8;

INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (84, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (85, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (86, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (87, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (88, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (89, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (90, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (91, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (95, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (98, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (101, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (102, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (103, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (104, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (105, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (106, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (107, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (109, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (110, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (113, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (114, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (119, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (120, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (121, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (123, '', '', '', 0);
INSERT INTO bb2_seller.store_extend (store_id, express, pricerange, orderpricerange, pic_num) VALUES (125, '', '', '', 0);