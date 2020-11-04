create table coupon_price
(
    coupon_price_id    smallint(4) auto_increment
        primary key,
    coupon_price_value smallint(4) default 0 not null comment '优惠券面额'
)
    comment '优惠券面额表' charset = utf8;

INSERT INTO bb2_mall.coupon_price (coupon_price_id, coupon_price_value) VALUES (1, 10);
INSERT INTO bb2_mall.coupon_price (coupon_price_id, coupon_price_value) VALUES (2, 30);
INSERT INTO bb2_mall.coupon_price (coupon_price_id, coupon_price_value) VALUES (3, 50);
INSERT INTO bb2_mall.coupon_price (coupon_price_id, coupon_price_value) VALUES (4, 100);
INSERT INTO bb2_mall.coupon_price (coupon_price_id, coupon_price_value) VALUES (5, 99);