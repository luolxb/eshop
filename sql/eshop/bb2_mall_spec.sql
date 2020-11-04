create table spec
(
    id              int auto_increment comment '规格表'
        primary key,
    name            varchar(55)           null comment '规格名称',
    `order`         int        default 50 null comment '排序',
    is_upload_image tinyint(1) default 0  not null comment '是否可上传规格图:0不可，1可以',
    search_index    tinyint(1) default 0  null comment '是否需要检索',
    cat_id1         int        default 0  null comment '一级分类',
    cat_id2         int        default 0  null comment '二级分类',
    cat_id3         int        default 0  null comment '三级分类'
)
    charset = utf8;

INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (1, '选择尺码', 50, 0, 0, 33, 34, 39);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (3, '选择颜色', 50, 0, 0, 33, 34, 39);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (4, '选择颜色', 50, 0, 0, 1, 2, 10);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (5, '选择版本', 50, 0, 0, 1, 2, 10);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (6, '系列', 50, 0, 0, 82, 116, 126);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (7, '选择颜色', 50, 0, 0, 67, 68, 75);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (8, '套装', 50, 0, 0, 67, 68, 75);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (9, '版面', 50, 0, 0, 86, 104, 109);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (10, '颜色', 50, 0, 0, 86, 104, 109);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (11, '尺寸', 50, 0, 0, 86, 104, 109);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (12, '套餐', 50, 0, 0, 149, 150, 154);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (15, '尺寸', 50, 0, 0, 166, 167, 170);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (16, '尺寸', 50, 0, 0, 166, 167, 171);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (17, '颜色', 50, 0, 0, 166, 167, 172);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (19, '选择颜色', 50, 0, 0, 82, 116, 173);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (20, '规格', 50, 0, 0, 166, 167, 170);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (21, '南来', 50, 0, 0, 1, 2, 10);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (22, '颜色', 40, 0, 0, 67, 69, 79);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (23, '111111', 50, 0, 0, 33, 34, 39);
INSERT INTO bb2_mall.spec (id, name, `order`, is_upload_image, search_index, cat_id1, cat_id2, cat_id3) VALUES (24, '选择规格', 50, 0, 0, 33, 34, 39);