create table brand
(
    id       smallint unsigned auto_increment comment '品牌表'
        primary key,
    name     varchar(60)      default '' not null comment '品牌名称',
    logo     varchar(255)     default '' not null comment '品牌logo',
    `desc`   text                        not null comment '品牌描述',
    url      varchar(255)     default '' not null comment '品牌地址',
    sort     tinyint unsigned default 50 not null comment '排序',
    cat_name varchar(128)     default '' null comment '品牌分类',
    cat_id1  int              default 0  null comment '一级分类id',
    cat_id2  int(10)          default 0  null comment '二级分类id',
    cat_id3  int              default 0  null comment '三级分类id',
    is_hot   tinyint(1)       default 0  null comment '是否推荐',
    store_id int(10)          default 0  null comment '商家ID',
    status   tinyint(1)       default 0  null comment '0正常 1审核中 2审核失败 审核状态'
)
    charset = utf8;

INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (1, '小米', '/public/upload/brand/2018/03-07/be7ca7d5e1552aaa50b68a8344d1ad7c.png', '手机品牌', 'www.xiaomi.com', 2, '', 1, 2, 0, 0, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (2, '魅族', '/public/upload/brand/2018/03-07/b94c0fc7e97c3a2ad67203c7a908084b.png', '手机品牌', 'www.meizu.com', 2, '', 1, 2, 0, 0, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (3, 'MANGO', '/public/upload/brand/2018/03-01/c4e17f5afe9addaf70eb60ec4567fa75.jpg', '衣服品牌', '', 1, '', 33, 34, 0, 0, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (4, 'MO&amp;Co.', '/public/upload/brand/2018/03-01/e1e9bc63e1bc07186bf44ea196a4c51f.jpg', '', '', 3, '', 33, 34, 0, 0, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (5, '茵曼', '/public/upload/brand/2018/03-01/76f65067f9a23744443718f93461ade6.jpg', '', '', 5, '', 33, 34, 0, 0, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (6, '欧莎', '/public/upload/brand/2018/03-01/9d06e2c42417cc64d42434111e9b1a84.jpg', '', '', 6, '', 33, 34, 0, 0, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (7, 'UR', '/public/upload/brand/2018/03-01/55739984769411bb3254c829c4695545.jpg', '', '', 9, '', 33, 34, 0, 0, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (8, 'LRUD', '/public/upload/brand/2018/03-01/6eb28c102330265fab323a67f95a521c.jpg', '', '', 0, '', 33, 34, 0, 1, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (9, '华为', '/public/upload/brand/2018/03-01/ccae0e32d7dca33be9194668e7cb1bb9.jpg', '', '', 0, '', 1, 2, 0, 1, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (10, 'olrain欧芮儿', '/public/upload/brand/2018/03-01/b3071403453f2317a77787d2d9d985c7.png', '', '', 0, '', 33, 34, 0, 1, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (11, '海信', '/public/upload/brand/2018/03-02/124fa70d1894b397be317a88a630e35a.jpg', '', 'www.hisense.com', 0, '', 86, 0, 0, 1, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (12, '飞利浦', '/public/upload/brand/2018/03-06/d6937a1e2bc1b441ebf91af8b2ae7e40.png', '', 'http://www.philips.com', 0, '', 86, 0, 0, 0, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (13, 'zara', '/public/upload/brand/2018/03-07/db9e511833f5cd6d40242c47764170b6.png', '', '', 0, '', 33, 34, 0, 0, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (14, '猛犸象', '/public/upload/store/14/brand/6fca80acbf15ec566f57a54ffff83b8f.jpg', '', '', 0, '', 83, 156, 0, 0, 14, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (15, '纪梵希', '/public/upload/store/15/brand/b4cc0805832f18ecb50b857bf0d52360.jpg', 'akkakka', '', 0, '', 33, 34, 0, 0, 15, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (16, '冈本', '/public/upload/store/27/brand/f90c51db026c2ad30026f4c035ddfe07.png', '', 'http://www.okamoto.cn/', 0, '', 166, 167, 0, 0, 27, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (17, '大象', '/public/upload/store/27/brand/61f212015bd9551f8e090a79e1805816.jpg', '', 'http://www.edaxiang.com/', 0, '', 166, 167, 0, 0, 27, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (18, '杜蕾斯', '/public/upload/store/27/brand/b78543e2c8b29b39358e6151922d9997.png', '', 'http://www.durex.com.cn/', 0, '', 166, 167, 0, 0, 27, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (19, '始祖鸟', '/public/upload/store/14/brand/dee91ec6d7ba362578e5efa2aece558c.jpg', '', 'https://www.arcteryx.com/Home.', 0, '', 83, 156, 0, 0, 14, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (20, 'OSPREY', '/public/upload/brand/2018/03-16/413dba739abe7fb4200c47f910868cf6.jpg', '', '', 0, '', 83, 160, 0, 0, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (21, '苹果6s', '/public/upload/store/30/brand/9f5980dd4040aa09b974e9605abc21f6.jpg', '商品名称：Apple 苹果 iPhone 6s Plus移动联通电信4G手机 金色 官方标配 32GB
商品编号：14404453058
', 'https://search.jd.com/Search?k', 10, '', 1, 3, 0, 0, 30, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (22, '美宝莲', '/public/upload/store/9/brand/fd88cff906e14f1cd675db7be8967af5.png', '美宝莲纽约', '', 0, '', 82, 116, 0, 1, 9, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (23, '旁氏米粹', '/public/upload/store/9/brand/1168f06e3cb0e275573bb66145cf44be.jpg', '', '', 0, '', 82, 116, 0, 0, 9, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (24, '媚点', '/public/upload/brand/2018/03-16/afd98b3304f40e5d3cc151f942f78726.jpg', '', '', 0, '', 82, 116, 0, 0, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (25, '三星手机', '/public/upload/brand/2018/04-02/f5cd7f55eaf5994519bf0ca553ed8b67.jpg', '', '', 1, '', 1, 2, 0, 0, 0, 0);
INSERT INTO bb2_mall.brand (id, name, logo, `desc`, url, sort, cat_name, cat_id1, cat_id2, cat_id3, is_hot, store_id, status) VALUES (26, 'iPhone', '', '', '', 0, '', 1, 2, 0, 0, 1, 0);