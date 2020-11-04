create table store_grade
(
    sg_id             int unsigned auto_increment comment '索引ID'
        primary key,
    sg_name           char(50)                             null comment '等级名称',
    sg_goods_limit    mediumint(10) unsigned  default 0    not null comment '允许发布的商品数量',
    sg_album_limit    mediumint unsigned      default 0    not null comment '允许上传图片数量',
    sg_space_limit    int unsigned            default 0    not null comment '上传空间大小，单位MB',
    sg_template_limit tinyint unsigned        default 0    not null comment '选择店铺模板套数',
    sg_template       varchar(255)                         null comment '模板内容',
    sg_price          decimal(10, 2) unsigned default 0.00 not null comment '开店费用(元/年)',
    sg_description    text                                 null comment '申请说明',
    sg_function       varchar(255)                         null comment '附加功能',
    sg_sort           tinyint unsigned        default 0    not null comment '级别，数目越大级别越高',
    sg_act_limits     text                                 not null comment '权限'
)
    comment '店铺等级表' charset = utf8;

INSERT INTO bb2_seller.store_grade (sg_id, sg_name, sg_goods_limit, sg_album_limit, sg_space_limit, sg_template_limit, sg_template, sg_price, sg_description, sg_function, sg_sort, sg_act_limits) VALUES (1, '一星', 2000, 1000, 0, 0, null, 3000.00, '', null, 1, 'all');
INSERT INTO bb2_seller.store_grade (sg_id, sg_name, sg_goods_limit, sg_album_limit, sg_space_limit, sg_template_limit, sg_template, sg_price, sg_description, sg_function, sg_sort, sg_act_limits) VALUES (2, '二钻', 10000, 5000, 0, 2, null, 5000.00, '', null, 2, '67,68,69,70,71,72,87,112,73,74,75,76,77,113,114,115,116,119,121,78,79,81,82,117,118,80,83,84,85,88,89,90,91,111,122,92,93,94,120,95,96,97,98,99,100,101,102,103,104,105,106,110,107,108,109');
INSERT INTO bb2_seller.store_grade (sg_id, sg_name, sg_goods_limit, sg_album_limit, sg_space_limit, sg_template_limit, sg_template, sg_price, sg_description, sg_function, sg_sort, sg_act_limits) VALUES (3, '三皇冠', 50000, 6000, 0, 5, null, 8000.00, '', null, 3, '67,68,69,70,71,72,87,112,73,74,75,76,77,113,114,115,116,119,121,78,79,81,82,117,118,80,83,84,85,88,89,90,91,111,122,92,93,94,120,95,96,97,98,99,100,101,102,103,104,105,106,110,107,108,109');
INSERT INTO bb2_seller.store_grade (sg_id, sg_name, sg_goods_limit, sg_album_limit, sg_space_limit, sg_template_limit, sg_template, sg_price, sg_description, sg_function, sg_sort, sg_act_limits) VALUES (4, '四金冠', 100000, 5000, 0, 10, null, 12000.00, '', null, 4, '67,68,69,70,71,72,87,112,73,74,75,76,77,113,114,115,116,119,121,78,79,81,82,117,118,80,83,84,85,88,89,90,91,111,122,92,93,94,120,95,96,97,98,99,100,101,102,103,104,105,106,110,107,108,109');
INSERT INTO bb2_seller.store_grade (sg_id, sg_name, sg_goods_limit, sg_album_limit, sg_space_limit, sg_template_limit, sg_template, sg_price, sg_description, sg_function, sg_sort, sg_act_limits) VALUES (5, '皇冠', 200000, 10000, 0, 6, null, 2000.00, '344444', null, 2, '67,68,69,70,71,72,87,112,73,74,75,76,77,113,114,115,116,119,121,78,79,81,82,117,118,80,83,84,85,88,89,90,91,111,122,92,93,94,120,95,96,97,98,99,100,101,102,103,104,105,106,110,107,108,109');
INSERT INTO bb2_seller.store_grade (sg_id, sg_name, sg_goods_limit, sg_album_limit, sg_space_limit, sg_template_limit, sg_template, sg_price, sg_description, sg_function, sg_sort, sg_act_limits) VALUES (6, '金主', 600000, 20000, 0, 6, null, 100.00, '等级更高的等级', null, 255, 'all');