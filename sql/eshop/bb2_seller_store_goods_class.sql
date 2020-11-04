create table store_goods_class
(
    cat_id       int auto_increment comment '索引ID'
        primary key,
    cat_name     varchar(50)      default '' not null comment '店铺商品分类名称',
    parent_id    int                         not null comment '父级id',
    shop_id      int(11) unsigned default 0  not null comment '门店id',
    store_id     int              default 0  not null comment '店铺id',
    cat_sort     int              default 0  not null comment '商品分类排序',
    is_show      tinyint(1)       default 0  not null comment '分类显示状态',
    is_nav_show  tinyint(1)       default 0  null comment '是否显示在导航栏',
    is_recommend tinyint(1)       default 0  null comment '是否首页推荐',
    show_num     smallint(4)      default 4  null comment '首页此类商品显示数量'
)
    comment '店铺商品分类表' charset = utf8;

create index stc_parent_id
    on store_goods_class (parent_id, cat_sort);

create index store_id
    on store_goods_class (store_id);

INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (1, '裙', 0, 0, 4, 10, 1, 1, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (2, '半身裙', 1, 0, 4, 10, 1, 0, 0, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (3, '连衣裙', 1, 0, 4, 9, 1, 0, 0, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (4, '短裙', 1, 0, 4, 11, 0, 0, 0, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (5, '毛衣针织', 0, 0, 4, 1, 1, 1, 0, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (6, '毛衣', 5, 0, 4, 10, 1, 0, 0, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (7, '针织外套', 5, 0, 4, 10, 1, 0, 0, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (8, '棉麻长裙', 1, 0, 4, 10, 0, 0, 0, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (9, '裤', 0, 0, 4, 1, 1, 1, 0, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (10, '长裤', 9, 0, 4, 2, 1, 0, 0, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (11, '牛仔裤', 9, 0, 4, 10, 1, 0, 0, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (12, '分辨率', 0, 0, 5, 10, 1, 1, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (13, '电视功能', 0, 0, 5, 10, 1, 1, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (14, '尺寸分类', 0, 0, 5, 10, 1, 1, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (15, '4K超清', 0, 0, 5, 10, 1, 1, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (16, '一级分类', 0, 0, 17, 10, 1, 1, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (17, '二级分类', 16, 0, 17, 10, 1, 1, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (18, '新品推荐', 0, 0, 9, 20, 1, 1, 0, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (19, '裤', 18, 0, 9, 9, 1, 1, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (22, '小三', 0, 0, 26, 10, 1, 1, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (23, '手机', 0, 0, 38, 10, 1, 0, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (24, '女装', 0, 0, 38, 10, 0, 0, 0, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (25, '数码', 0, 0, 44, 10, 1, 1, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (26, '数码', 0, 0, 44, 10, 1, 1, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (27, '手机器件', 0, 0, 47, 10, 1, 1, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (28, '单反', 0, 0, 4, 10, 1, 1, 1, 4);
INSERT INTO bb2_seller.store_goods_class (cat_id, cat_name, parent_id, shop_id, store_id, cat_sort, is_show, is_nav_show, is_recommend, show_num) VALUES (29, '佳能相机', 28, 0, 4, 10, 1, 0, 0, 4);