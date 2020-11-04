create table freight_template
(
    template_id       int(11) unsigned auto_increment comment '运费模板ID'
        primary key,
    template_name     varchar(255) charset utf8     not null comment '模板名称',
    type              tinyint(1)          default 0 not null comment '0 件数；1 商品重量；2 商品体积',
    is_enable_default tinyint(1) unsigned default 0 not null comment '是否启用使用默认运费配置,0:不启用，1:启用',
    store_id          int(11) unsigned    default 0 not null comment '店铺Id'
)
    comment '运费模板表' charset = latin1;

INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (1, '以件计数', 0, 1, 4);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (2, '满三件包邮', 0, 1, 9);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (3, '以重量计算', 1, 1, 9);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (4, '以件购买', 0, 1, 6);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (5, '以重量计算', 1, 1, 6);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (6, '以体积计算', 2, 1, 6);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (7, '以重量计算', 1, 0, 4);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (8, '以体积计算', 2, 1, 4);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (9, '全国送', 0, 1, 3);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (10, '珠三角', 1, 0, 27);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (11, '广东', 1, 0, 27);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (12, '广东', 1, 0, 14);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (13, '全国配送', 0, 1, 2);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (14, '以体积计算', 2, 1, 9);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (15, '顺丰快递', 0, 1, 26);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (16, '电脑', 0, 1, 30);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (17, '按件计费', 0, 1, 30);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (18, '按重量计费', 1, 1, 30);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (19, '按体积计费', 2, 1, 30);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (20, '满2件包邮', 0, 1, 29);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (21, '甜14752', 0, 1, 44);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (22, '模板', 1, 0, 49);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (23, '手机', 0, 1, 1);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (24, '酒', 1, 1, 1);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (25, '批量酒', 2, 1, 1);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (26, '酒类易碎品', 1, 0, 1);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (27, '酒', 1, 0, 142);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (28, '酒', 1, 1, 63);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (30, '酒', 1, 1, 77);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (31, '酒', 1, 1, 51);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (32, '酒', 1, 1, 52);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (34, '酒', 0, 1, 60);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (35, '酒', 1, 1, 68);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (36, '酒', 1, 0, 79);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (37, '10', 1, 0, 89);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (38, '16', 0, 0, 98);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (39, '14585', 0, 0, 98);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (40, '56', 0, 0, 98);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (41, '5', 0, 0, 98);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (42, '96', 0, 0, 98);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (43, '45', 0, 0, 98);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (44, '346', 0, 0, 98);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (45, '786', 0, 0, 98);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (46, '97', 0, 0, 98);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (47, '890', 0, 0, 98);
INSERT INTO bb2_mall.freight_template (template_id, template_name, type, is_enable_default, store_id) VALUES (48, '5437', 0, 0, 98);