create table friend_link
(
    link_id   smallint unsigned auto_increment
        primary key,
    link_name varchar(255)     default '' not null,
    link_url  varchar(255)     default '' not null,
    link_logo varchar(255)     default '' not null,
    orderby   tinyint unsigned default 50 not null comment '排序',
    is_show   tinyint(1)       default 1  null comment '是否显示',
    target    tinyint(1)       default 1  null comment '是否新窗口打开'
)
    charset = utf8;

create index show_order
    on friend_link (orderby);

INSERT INTO bb2_mall.friend_link (link_id, link_name, link_url, link_logo, orderby, is_show, target) VALUES (1, '淘宝网', 'https://www.taobao.com/', '/public/upload/link/2018/02-28/1d5896b893c6f4638a661e401a0383a1.jpg', 1, 1, 0);
INSERT INTO bb2_mall.friend_link (link_id, link_name, link_url, link_logo, orderby, is_show, target) VALUES (2, '京东', 'https://www.jd.com', '/public/upload/link/2018/02-28/214c90078bb0b25046a0fdb8d6155787.jpg', 2, 1, 0);
INSERT INTO bb2_mall.friend_link (link_id, link_name, link_url, link_logo, orderby, is_show, target) VALUES (3, '美团', 'http://www.meituan.com/', '/public/upload/link/2018/02-28/1debadb912f850b7ca44a2ef68cccc7b.jpg', 3, 1, 0);
INSERT INTO bb2_mall.friend_link (link_id, link_name, link_url, link_logo, orderby, is_show, target) VALUES (4, '飞牛网', 'http://www.feiniu.com/', '', 4, 1, 0);
INSERT INTO bb2_mall.friend_link (link_id, link_name, link_url, link_logo, orderby, is_show, target) VALUES (5, '唯品会', 'https://www.vip.com/', '', 1, 1, 0);