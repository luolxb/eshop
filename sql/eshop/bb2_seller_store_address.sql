create table store_address
(
    store_address_id mediumint unsigned auto_increment
        primary key,
    consignee        varchar(60)         default '' not null comment '收货人',
    province_id      int                 default 0  not null comment '省份',
    city_id          int                 default 0  not null comment '城市',
    district_id      int                 default 0  not null comment '地区',
    town_id          int                 default 0  not null comment '乡镇',
    address          varchar(250)        default '' not null comment '地址',
    zip_code         varchar(60)         default '' not null comment '邮政编码',
    mobile           varchar(60)         default '' not null comment '手机',
    is_default       tinyint(1) unsigned default 0  not null comment '1为默认收货地址',
    type             tinyint(1) unsigned default 0  not null comment '0为发货地址。1为收货地址',
    store_id         int unsigned        default 0  not null comment '店铺id'
)
    comment '店铺地址表' charset = utf8;

INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (7, '56987', 3102, 3103, 3104, 0, '4687', '432000', '13052514785', 1, 0, 87);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (8, '598', 43776, 43777, 43779, 0, '47878', '432000', '13052635896', 1, 0, 88);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (9, '598', 28240, 29755, 29756, 0, '9797', '423000', '13005420145', 1, 0, 89);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (10, '2198', 1, 300, 301, 0, '465', '423000', '13052041025', 1, 0, 98);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (11, '45', 43776, 44352, 44392, 0, '1554', '432000', '13052635241', 1, 0, 101);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (12, '543745', 10543, 10779, 10780, 0, '74', '432000', '13050289099', 1, 0, 102);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (13, '14545', 43776, 44829, 44886, 0, '2954', '432000', '13052638596', 1, 0, 105);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (14, '18565853236', 43776, 43777, 43779, 0, '18565853236', '462300', '18565853236', 1, 0, 107);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (15, '679769', 7531, 8216, 8217, 0, '799', '432000', '13052635289', 1, 0, 110);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (16, '浅安', 28240, 28558, 28571, 0, '爱地大厦西座21F', '518000', '13058140233', 0, 0, 110);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (17, 'fdsafds', 43776, 43777, 43778, 0, 'dsafdsafdsafsad', '518000', '18687258687', 1, 0, 114);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (18, '6544', 43776, 43777, 43779, 0, '15164646', '423000', '13055205263', 1, 0, 113);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (19, 'lin', 43776, 43777, 43778, 0, '43634643', '423000', '13052638596', 1, 0, 120);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (20, '132', 43776, 43777, 43779, 0, '234', '432000', '13098989899', 1, 0, 121);
INSERT INTO bb2_seller.store_address (store_address_id, consignee, province_id, city_id, district_id, town_id, address, zip_code, mobile, is_default, type, store_id) VALUES (21, '49', 39556, 39710, 39712, 0, '589', '423000', '13052024652', 1, 0, 123);