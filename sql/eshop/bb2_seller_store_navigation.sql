create table store_navigation
(
    sn_id       int unsigned auto_increment comment '导航ID'
        primary key,
    sn_title    varchar(50)         default '' not null comment '导航名称',
    sn_store_id int unsigned        default 0  not null comment '卖家店铺ID',
    sn_content  text                           null comment '导航内容',
    sn_sort     tinyint unsigned    default 0  not null comment '导航排序',
    sn_is_show  tinyint(1)          default 0  not null comment '导航是否显示',
    sn_add_time int                            not null comment '添加时间',
    sn_url      varchar(255)                   null comment '店铺导航的外链URL',
    sn_new_open tinyint(1) unsigned default 0  not null comment '店铺导航外链是否在新窗口打开：0不开新窗口1开新窗口，默认是0'
)
    comment '卖家店铺导航信息表' charset = utf8;

INSERT INTO bb2_seller.store_navigation (sn_id, sn_title, sn_store_id, sn_content, sn_sort, sn_is_show, sn_add_time, sn_url, sn_new_open) VALUES (1, '新品推荐', 9, '&lt;p&gt;&lt;img src=&quot;/public/upload/store/9/nav/1eca100b849ee2b5c40bed919aa8068d.png&quot; title=&quot;&quot; alt=&quot;&quot;/&gt;&lt;/p&gt;', 1, 1, 1519971326, 'http://192.168.0.250/index.php/Home/Goods/goodsInfo/id/13.html', 0);
INSERT INTO bb2_seller.store_navigation (sn_id, sn_title, sn_store_id, sn_content, sn_sort, sn_is_show, sn_add_time, sn_url, sn_new_open) VALUES (2, '热卖促销', 9, '&lt;p&gt;6666666666666666666666666&lt;/p&gt;', 2, 1, 1519972123, 'http://192.168.0.250/index.php/home/Goods/goodsInfo/id/63.html', 0);
INSERT INTO bb2_seller.store_navigation (sn_id, sn_title, sn_store_id, sn_content, sn_sort, sn_is_show, sn_add_time, sn_url, sn_new_open) VALUES (3, '新品推荐', 17, '&lt;p&gt;332332&lt;/p&gt;', 0, 1, 1521027743, 'baidu.com', 0);
INSERT INTO bb2_seller.store_navigation (sn_id, sn_title, sn_store_id, sn_content, sn_sort, sn_is_show, sn_add_time, sn_url, sn_new_open) VALUES (4, '商品推荐', 17, '&lt;p&gt;212121212&lt;/p&gt;', 200, 1, 1521027787, 'baidu.com', 0);
INSERT INTO bb2_seller.store_navigation (sn_id, sn_title, sn_store_id, sn_content, sn_sort, sn_is_show, sn_add_time, sn_url, sn_new_open) VALUES (5, '百度', 26, '&lt;p&gt;111111111&lt;/p&gt;', 1, 1, 1521170175, 'https://www.baidu.com/', 1);
INSERT INTO bb2_seller.store_navigation (sn_id, sn_title, sn_store_id, sn_content, sn_sort, sn_is_show, sn_add_time, sn_url, sn_new_open) VALUES (6, '1', 4, '&lt;p&gt;哈哈哈&lt;/p&gt;', 2, 0, 1522059287, 'www.bba.com', 1);
INSERT INTO bb2_seller.store_navigation (sn_id, sn_title, sn_store_id, sn_content, sn_sort, sn_is_show, sn_add_time, sn_url, sn_new_open) VALUES (7, '112', 4, '', 0, 1, 1522058633, 'www.bba.com', 0);
INSERT INTO bb2_seller.store_navigation (sn_id, sn_title, sn_store_id, sn_content, sn_sort, sn_is_show, sn_add_time, sn_url, sn_new_open) VALUES (8, '4334', 4, '&lt;p&gt;343434&lt;/p&gt;', 0, 1, 1522058660, 'www.bba.com', 0);
INSERT INTO bb2_seller.store_navigation (sn_id, sn_title, sn_store_id, sn_content, sn_sort, sn_is_show, sn_add_time, sn_url, sn_new_open) VALUES (9, '0', 4, '', 0, 0, 1522059305, 'www.bba.com', 0);
INSERT INTO bb2_seller.store_navigation (sn_id, sn_title, sn_store_id, sn_content, sn_sort, sn_is_show, sn_add_time, sn_url, sn_new_open) VALUES (10, 'danta', 36, '', 0, 0, 1522371132, 'www.bba.com', 0);
INSERT INTO bb2_seller.store_navigation (sn_id, sn_title, sn_store_id, sn_content, sn_sort, sn_is_show, sn_add_time, sn_url, sn_new_open) VALUES (13, '商品推荐', 37, '&lt;p&gt;商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐商品推荐&lt;/p&gt;', 1, 1, 1522388468, 'http://bb2t.tp-shop.cn/index.php/Home/Goods/goodsInfo/id/3.html', 0);
INSERT INTO bb2_seller.store_navigation (sn_id, sn_title, sn_store_id, sn_content, sn_sort, sn_is_show, sn_add_time, sn_url, sn_new_open) VALUES (14, '热门商品', 37, '&lt;p&gt;热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品热门商品&lt;/p&gt;', 2, 1, 1522388544, 'http://bb2t.tp-shop.cn', 0);
INSERT INTO bb2_seller.store_navigation (sn_id, sn_title, sn_store_id, sn_content, sn_sort, sn_is_show, sn_add_time, sn_url, sn_new_open) VALUES (15, '手机器件', 47, '&lt;p&gt;手机器件和元件&lt;/p&gt;', 1, 1, 1562312105, '', 0);