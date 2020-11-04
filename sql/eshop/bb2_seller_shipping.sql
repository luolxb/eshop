create table shipping
(
    shipping_id       int(11) unsigned auto_increment comment '物流公司id'
        primary key,
    shipping_name     varchar(255) default '' not null comment '物流公司名称',
    shipping_code     varchar(255) default '' not null comment '物流公司编码',
    shipping_desc     varchar(255) default '' not null comment '物流描述',
    shipping_logo     varchar(255) default '' not null comment '物流公司logo',
    template_width    int unsigned default 0  not null comment '运单模板宽度',
    template_height   int unsigned default 0  not null comment '运单模板高度',
    template_offset_x int unsigned default 0  not null comment '运单模板左偏移量',
    template_offset_y int unsigned default 0  not null comment '运单模板上偏移量',
    template_img      varchar(255) default '' not null comment '运单模板图片',
    template_html     text                    not null comment '打印项偏移校正'
)
    charset = utf8;

INSERT INTO bb2_seller.shipping (shipping_id, shipping_name, shipping_code, shipping_desc, shipping_logo, template_width, template_height, template_offset_x, template_offset_y, template_img, template_html) VALUES (1, '申通快递', 'shentong', '申通快递插件', '/public/upload/shipping_logo/2018/03-05/644ee35de207a1dab6a9421e37a3890e.png', 840, 480, 0, 0, '/public/upload/plugins/2018/03-05/a5bde8493ea5840ec99f6e56a4ca692c.png', '
                                                ');
INSERT INTO bb2_seller.shipping (shipping_id, shipping_name, shipping_code, shipping_desc, shipping_logo, template_width, template_height, template_offset_x, template_offset_y, template_img, template_html) VALUES (2, '顺丰快递', 'shunfeng', '顺丰快递插件', '/public/upload/shipping_logo/2018/03-05/080900ec4330e4c7de2265bf9d94bdae.jpg', 840, 480, 0, 0, '/public/upload/plugins/2018/03-05/5bc06354154bb0dcea5196bfbeb7ece5.png', '
                                                ');
INSERT INTO bb2_seller.shipping (shipping_id, shipping_name, shipping_code, shipping_desc, shipping_logo, template_width, template_height, template_offset_x, template_offset_y, template_img, template_html) VALUES (3, '圆通快递', 'yuantong', '圆通快递插件', '/public/upload/shipping_logo/2018/03-05/4d9afb83fe8abf801c57d752e0022f13.jpg', 840, 480, 0, 0, '/public/upload/plugins/2018/03-05/0733a50ac6a99b71e1109cd8a640faa3.png', '
                            
                                                                    ');
INSERT INTO bb2_seller.shipping (shipping_id, shipping_name, shipping_code, shipping_desc, shipping_logo, template_width, template_height, template_offset_x, template_offset_y, template_img, template_html) VALUES (4, '中通快递', 'zhongtong', '中通快递插件', '/public/upload/shipping_logo/2018/03-19/db1e0e9d6375e36a7a2d21138556aa7f.png', 840, 480, 0, 0, '/public/upload/plugins/2018/03-19/1b4d081a6668b56d00ea0146558e3d1e.png', '
                                                ');
INSERT INTO bb2_seller.shipping (shipping_id, shipping_name, shipping_code, shipping_desc, shipping_logo, template_width, template_height, template_offset_x, template_offset_y, template_img, template_html) VALUES (5, '韵达快递', 'yunda', '韵达快递插件', '/public/upload/shipping_logo/2018/03-19/f2124ab7eb66b206d611f7e72fe536b6.png', 840, 480, 0, 0, '/public/upload/plugins/2018/03-19/7b9a12c2c87bc4a978f38c8f8484ba06.png', '
                                                ');