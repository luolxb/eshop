create table article_cat
(
    cat_id      int unsigned auto_increment
        primary key,
    cat_name    varchar(20)           null comment '类别名称',
    cat_type    smallint   default 0  null comment '默认分组',
    parent_id   smallint   default 0  null comment '夫级ID',
    show_in_nav tinyint(1) default 0  null comment '是否导航显示',
    sort_order  smallint   default 50 null comment '排序',
    cat_desc    varchar(255)          null comment '分类描述',
    keywords    varchar(30)           null comment '搜索关键词',
    cat_alias   varchar(20)           null comment '别名'
)
    charset = utf8;

INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (1, '系统分类', 1, 0, 1, 3, '', '', 'system');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (2, '店铺帮助', 0, 0, 0, 1, '', '', 'help');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (3, '购物常见问题', 1, 0, 1, 0, '', '', 'help');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (4, '支付方式', 1, 1, 1, 0, '', '', 'help');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (5, '配送服务', null, 1, 0, 0, '', '', 'help');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (6, '售后服务', null, 1, 0, 0, '', '', 'help');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (7, '常见问题', null, 1, 0, 0, '', '', 'help');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (8, '入驻流程', null, 2, 0, 0, '', '', 'help');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (9, '规则体系', 0, 14, 0, 0, '', '', 'help');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (10, '联系方式', null, 2, 0, 0, '', '', 'help');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (11, '购物指南', 1, 0, 1, 0, '222', '22', 'help');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (12, '退款/售后', 1, 6, 0, 0, '', '', 'help');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (13, '关于我们', null, 1, 0, 0, '', '', 'about');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (14, '招商标准', null, 2, 0, 0, '', '', '');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (15, '帮助中心', null, 1, 0, 0, '', '', '');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (16, '资费标准', null, 2, 0, 0, '', '', 'help');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (17, 'B2B api接口文档', null, 0, 0, 0, '', '', '');
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (18, '入住资质', 0, 14, 1, 0, '', '', null);
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (19, '进驻详情', 0, 18, 1, 0, '', '', null);
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (20, '促销活动', 1, 0, 1, 0, '', '', null);
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (23, '信息公告', 0, 2, 1, 50, '', ' 信息公告', null);
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (24, '信息公告', 0, 1, 0, 0, '', '', null);
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (25, '招商标准', 0, 1, 1, 0, '招商标准', '招商标准', null);
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (26, '资费标准', 0, 1, 1, 0, '资费标准', '资费标准', null);
INSERT INTO bb2_mall.article_cat (cat_id, cat_name, cat_type, parent_id, show_in_nav, sort_order, cat_desc, keywords, cat_alias) VALUES (27, 'TPSHOP优势', 0, 1, 1, 0, 'TPSHOP优势', 'TPSHOP优势', null);