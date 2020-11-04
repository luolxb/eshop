create table user_address
(
    address_id mediumint unsigned auto_increment
        primary key,
    user_id    mediumint unsigned default 0         not null,
    consignee  varchar(60)        default ''        not null comment '收货人',
    email      varchar(60)        default ''        not null comment '邮箱地址',
    country    int                default 0         not null comment '国家',
    province   int                default 0         not null comment '省份',
    city       int                default 0         not null comment '城市',
    district   int                default 0         not null comment '地区',
    twon       int                default 0         null comment '乡镇',
    address    varchar(250)       default ''        not null comment '地址',
    zipcode    varchar(60)        default ''        not null comment '邮政编码',
    mobile     varchar(60)        default ''        not null comment '手机',
    is_default tinyint(1)         default 0         null comment '默认收货地址',
    longitude  decimal(10, 7)     default 0.0000000 not null comment '地址经度',
    latitude   decimal(10, 7)     default 0.0000000 not null comment '地址纬度'
)
    charset = utf8;

create index user_id
    on user_address (user_id);

INSERT INTO bb2_user.user_address (address_id, user_id, consignee, email, country, province, city, district, twon, address, zipcode, mobile, is_default, longitude, latitude) VALUES (190, 364, '浅浅', '', 0, 31563, 31633, 31768, 0, '33902', '', '13928391939', 1, 0.0000000, 0.0000000);
INSERT INTO bb2_user.user_address (address_id, user_id, consignee, email, country, province, city, district, twon, address, zipcode, mobile, is_default, longitude, latitude) VALUES (191, 363, 'llllll', '', 0, 1, 300, 322, 0, '嘻嘻哈哈', '', '123456', 1, 0.0000000, 0.0000000);
INSERT INTO bb2_user.user_address (address_id, user_id, consignee, email, country, province, city, district, twon, address, zipcode, mobile, is_default, longitude, latitude) VALUES (193, 366, 'shao', '', 0, 28240, 28785, 28786, 0, '101', '', '18603059551', 1, 0.0000000, 0.0000000);
INSERT INTO bb2_user.user_address (address_id, user_id, consignee, email, country, province, city, district, twon, address, zipcode, mobile, is_default, longitude, latitude) VALUES (194, 371, 'ddd', '', 0, 37906, 37907, 37927, 0, 'hhhh', '', '348838', 1, 0.0000000, 0.0000000);
INSERT INTO bb2_user.user_address (address_id, user_id, consignee, email, country, province, city, district, twon, address, zipcode, mobile, is_default, longitude, latitude) VALUES (195, 373, '唐', '', 0, 28240, 28558, 28590, 0, '038819', '', '13098612368', 1, 0.0000000, 0.0000000);