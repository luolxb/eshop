create table article
(
    article_id   mediumint unsigned auto_increment
        primary key,
    cat_id       smallint(5)         default 0  not null comment '类别ID',
    title        varchar(150)        default '' not null comment '文章标题',
    content      longtext                       not null,
    author       varchar(30)         default '' not null comment '文章作者',
    author_email varchar(60)         default '' not null comment '作者邮箱',
    keywords     varchar(255)        default '' not null comment '关键字',
    article_type tinyint(1) unsigned default 2  not null,
    is_open      tinyint(1) unsigned default 1  not null,
    add_time     int unsigned        default 0  not null,
    file_url     varchar(255)        default '' not null comment '附件地址',
    open_type    tinyint(1) unsigned default 0  not null,
    link         varchar(255)        default '' not null comment '链接地址',
    description  mediumtext                     null comment '文章摘要',
    click        int                 default 0  null comment '浏览量',
    publish_time int                            null comment '文章预告发布时间',
    thumb        varchar(255)        default '' null comment '文章缩略图'
)
    charset = utf8;

create index cat_id
    on article (cat_id);

INSERT INTO bb2_mall.article (article_id, cat_id, title, content, author, author_email, keywords, article_type, is_open, add_time, file_url, open_type, link, description, click, publish_time, thumb) VALUES (1, 8, '商家入驻流程', '&lt;p&gt;如果您需要入驻本商城。&lt;br/&gt;&lt;/p&gt;&lt;p&gt;需要先在商城里注册一个会员账号，如图：&lt;/p&gt;&lt;p&gt;&lt;img src=&quot;/public/upload/article/2018/03-02/f9a30a44769e79b7687abd55be2c8f9d.png&quot; title=&quot;&quot; alt=&quot;&quot; width=&quot;1088&quot; height=&quot;695&quot;/&gt;&lt;/p&gt;&lt;p&gt;注册账号成功以后，在申请入驻页里填写入驻信息&lt;/p&gt;&lt;p&gt;&lt;img src=&quot;/public/upload/article/2018/03-02/6d30dbdb5d12f3838f37e3ace78e3be5.png&quot; title=&quot;&quot; alt=&quot;&quot; width=&quot;1094&quot; height=&quot;780&quot;/&gt;&lt;/p&gt;&lt;p&gt;&lt;img src=&quot;/public/upload/article/2018/03-02/892fd093645b56225d021968ec0f493c.png&quot; title=&quot;&quot; alt=&quot;&quot; width=&quot;1092&quot; height=&quot;598&quot;/&gt;入驻申请提交以后，等平台审核即可，若审核成功代表店铺入驻成功，可在商家中心登录账号管理店铺&lt;/p&gt;', '', '', '', 2, 1, 1519972944, '', 0, '', '', 1253, 1520006400, '');
INSERT INTO bb2_mall.article (article_id, cat_id, title, content, author, author_email, keywords, article_type, is_open, add_time, file_url, open_type, link, description, click, publish_time, thumb) VALUES (2, 23, '商城入驻功能重新上线', '&lt;p&gt;商城入驻功能重新上线，广大店家可入驻&lt;/p&gt;', '', '', '', 2, 1, 1519973384, '', 0, '', '', 1171, 1520006400, '');
INSERT INTO bb2_mall.article (article_id, cat_id, title, content, author, author_email, keywords, article_type, is_open, add_time, file_url, open_type, link, description, click, publish_time, thumb) VALUES (3, 12, '退款售后流程', '&lt;p&gt;以下是商品售后流程：&lt;/p&gt;&lt;p&gt;在商城还未发货之前，您可以直接取消订单，商城会根据您的付款方式退还您的金额。&lt;/p&gt;&lt;p&gt;如果商品已发货，或者在您收到货以后对商品不满意，您可以针对单个商品发起售后&lt;/p&gt;', '', '', '', 2, 1, 1519973595, '', 0, '', '', 1243, 1520006400, '');
INSERT INTO bb2_mall.article (article_id, cat_id, title, content, author, author_email, keywords, article_type, is_open, add_time, file_url, open_type, link, description, click, publish_time, thumb) VALUES (4, 3, '购物流程', '&lt;p&gt;您需要先在商城注册一个会员号，才能进行下单。&lt;/p&gt;&lt;p&gt;注册完成以后，可在本商城挑选商品&lt;/p&gt;&lt;p&gt;挑选好中意的商品以后，结算即可。&lt;/p&gt;&lt;p&gt;商城会根据您的收货地址为您发货。&lt;/p&gt;', '', '', '', 2, 1, 1519973853, '', 0, '', '', 1122, 1520006400, '');
INSERT INTO bb2_mall.article (article_id, cat_id, title, content, author, author_email, keywords, article_type, is_open, add_time, file_url, open_type, link, description, click, publish_time, thumb) VALUES (5, 4, '商城支持的支付方式', '&lt;p&gt;商城暂时支持微信/支付宝/，余额/积分抵扣，信用卡/银行卡支付&lt;/p&gt;', '', '', '', 2, 1, 1519973972, '', 0, '', '', 1073, 1520006400, '');
INSERT INTO bb2_mall.article (article_id, cat_id, title, content, author, author_email, keywords, article_type, is_open, add_time, file_url, open_type, link, description, click, publish_time, thumb) VALUES (6, 23, '商城入驻功能上线', '&lt;p&gt;商城入驻功能上线商城入驻功能上线商城入驻功能上线商城入驻功能上线商城入驻功能上线商城入驻功能上线&lt;/p&gt;', '', '', '', 2, 1, 1519974146, '', 0, '', '', 1036, 1520006400, '');
INSERT INTO bb2_mall.article (article_id, cat_id, title, content, author, author_email, keywords, article_type, is_open, add_time, file_url, open_type, link, description, click, publish_time, thumb) VALUES (8, 20, '促销活动', '&lt;p&gt;666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666&lt;/p&gt;', '', '', '促销', 2, 1, 1520297979, '', 0, '', '66666666', 1149, 1520352000, '');
INSERT INTO bb2_mall.article (article_id, cat_id, title, content, author, author_email, keywords, article_type, is_open, add_time, file_url, open_type, link, description, click, publish_time, thumb) VALUES (9, 11, '购物指南', '&lt;p&gt;6666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666&lt;/p&gt;', '', '', '购物，下单', 2, 1, 1520298034, '', 0, '', '', 1106, 1520352000, '');
INSERT INTO bb2_mall.article (article_id, cat_id, title, content, author, author_email, keywords, article_type, is_open, add_time, file_url, open_type, link, description, click, publish_time, thumb) VALUES (10, 23, '公告', '&lt;p&gt;66666666666666666666666666666666666666666666666666666666&lt;/p&gt;', '', '', '', 2, 1, 1520414304, '', 0, '', '', 1032, 1520438400, '');
INSERT INTO bb2_mall.article (article_id, cat_id, title, content, author, author_email, keywords, article_type, is_open, add_time, file_url, open_type, link, description, click, publish_time, thumb) VALUES (11, 23, '信息公告', '&lt;p&gt;啊哈啊哈哈啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊&lt;/p&gt;', '', '', '', 2, 1, 1520414634, '', 0, '', '', 1004, 1520438400, '');
INSERT INTO bb2_mall.article (article_id, cat_id, title, content, author, author_email, keywords, article_type, is_open, add_time, file_url, open_type, link, description, click, publish_time, thumb) VALUES (12, 24, '公告公告', '&lt;p&gt;6666666666666666666666666666665&lt;/p&gt;', '', '', '', 2, 1, 1520414670, '', 0, '', '', 1058, 1520438400, '');
INSERT INTO bb2_mall.article (article_id, cat_id, title, content, author, author_email, keywords, article_type, is_open, add_time, file_url, open_type, link, description, click, publish_time, thumb) VALUES (13, 23, '第一次测试', '&lt;p&gt;第一次测试，进行熟悉具体功能&lt;/p&gt;', '', '', '测试', 2, 1, 1562306341, '', 0, '', '测试', 1290, 1562342400, '');