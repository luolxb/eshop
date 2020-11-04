create table freight_config
(
    config_id      int(11) unsigned auto_increment comment '配置id'
        primary key,
    first_unit     double(16, 4)           default 0.0000 not null comment '首(重：体积：件）',
    first_money    decimal(10, 2) unsigned default 0.00   not null comment '首(重：体积：件）运费',
    continue_unit  double(16, 4) unsigned  default 0.0000 not null comment '继续加（件：重量：体积）区间',
    continue_money decimal(10, 2) unsigned default 0.00   not null comment '继续加（件：重量：体积）的运费',
    template_id    int(11) unsigned        default 0      not null comment '运费模板ID',
    is_default     tinyint(1) unsigned     default 0      not null comment '是否是默认运费配置.0不是，1是',
    store_id       int(11) unsigned        default 0      not null comment '店铺ID'
)
    comment '运费配置表' charset = latin1;

INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (1, 3, 10.00, 2, 3.00, 1, 1, 4);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (2, 4, 12.00, 3, 2.00, 1, 0, 4);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (3, 2, 10.00, 3, 2.00, 2, 1, 9);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (4, 3, 8.00, 2, 3.00, 2, 0, 9);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (5, 400, 12.00, 200, 3.00, 3, 1, 9);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (6, 400, 10.00, 200, 2.00, 3, 0, 9);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (7, 3, 10.00, 2, 3.00, 4, 1, 6);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (8, 4, 12.00, 3, 2.00, 4, 0, 6);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (9, 400, 10.00, 200, 4.00, 5, 1, 6);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (10, 500, 8.00, 200, 3.00, 5, 0, 6);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (11, 3, 9.00, 2, 3.00, 6, 1, 6);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (12, 5, 10.00, 3, 2.00, 6, 0, 6);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (13, 400, 10.00, 100, 3.00, 7, 0, 4);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (14, 4, 10.00, 2, 3.00, 8, 1, 4);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (15, 5, 9.00, 3, 2.00, 8, 0, 4);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (16, 1, 10.00, 1, 5.00, 9, 1, 3);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (17, 500, 10.00, 500, 1.00, 10, 0, 27);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (18, 500, 10.00, 500, 2.00, 11, 0, 27);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (19, 500, 10.00, 500, 5.00, 11, 0, 27);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (20, 500, 0.00, 500, 0.00, 12, 0, 14);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (21, 1, 10.00, 1, 5.00, 13, 1, 2);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (22, 5, 8.00, 2, 3.00, 14, 1, 9);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (23, 2, 10.00, 3, 2.00, 14, 0, 9);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (24, 3, 6.00, 2, 3.00, 14, 0, 9);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (25, 100, 20.00, 12, 0.00, 15, 1, 26);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (26, 1, 100.00, 1, 50.00, 16, 1, 30);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (27, 100000, 20.00, 100000, 3000.00, 15, 0, 26);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (28, 3, 10.00, 1, 3.00, 17, 1, 30);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (29, 30, 10.00, 10, 5.00, 18, 1, 30);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (30, 30, 20.00, 10, 5.00, 19, 1, 30);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (31, 1, 10.00, 2, 0.00, 20, 1, 29);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (32, 1, 1.00, 1, 1.00, 21, 0, 44);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (33, 1, 1.00, 1, 1.00, 21, 1, 44);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (34, 100, 0.00, 10, 0.00, 22, 0, 49);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (35, 1, 12.00, 25, 24.00, 23, 1, 1);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (36, 1, 1.00, 1, 1.00, 24, 0, 1);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (37, 1, 10.00, 1, 1.00, 24, 0, 1);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (40, 1, 13.00, 1, 1.00, 25, 1, 1);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (41, 1, 10.00, 1, 5.00, 26, 0, 1);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (42, 1, 1.00, 1, 1.00, 27, 0, 142);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (43, 1, 1.00, 1, 1.00, 28, 1, 63);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (45, 1, 10.00, 1, 1.00, 30, 1, 77);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (46, 1, 10.00, 1, 1.00, 31, 1, 51);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (47, 1, 10.00, 1, 1.00, 32, 1, 52);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (49, 1, 1.00, 1, 1.00, 34, 1, 60);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (50, 1, 1.00, 1, 1.00, 34, 0, 60);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (51, 1, 1.00, 1, 1.00, 34, 0, 60);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (52, 1, 10.00, 1, 1.00, 35, 0, 68);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (53, 1, 10.00, 1, 1.00, 36, 0, 79);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (54, 1, 10.00, 1, 10.00, 37, 0, 89);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (55, 1, 1.00, 1, 1.00, 38, 0, 98);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (56, 1, 1.00, 1, 1.00, 39, 0, 98);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (57, 1, 1.00, 1, 1.00, 40, 0, 98);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (58, 1, 1.00, 1, 1.00, 41, 0, 98);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (59, 1, 1.00, 1, 1.00, 42, 0, 98);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (60, 1, 1.00, 1, 1.00, 43, 0, 98);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (61, 1, 1.00, 1, 1.00, 44, 0, 98);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (62, 1, 1.00, 1, 1.00, 45, 0, 98);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (63, 1, 1.00, 1, 1.00, 46, 0, 98);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (64, 1, 1.00, 1, 1.00, 47, 0, 98);
INSERT INTO bb2_mall.freight_config (config_id, first_unit, first_money, continue_unit, continue_money, template_id, is_default, store_id) VALUES (65, 1, 1.00, 1, 1.00, 48, 0, 98);