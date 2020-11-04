create table goods_coupon
(
    coupon_id         int(8)                not null comment '优惠券id',
    goods_id          int         default 0 not null comment '指定的商品id：为零表示不指定商品',
    goods_category_id smallint(5) default 0 not null comment '指定的商品分类：为零表示不指定分类',
    primary key (coupon_id, goods_id, goods_category_id)
)
    charset = latin1;

