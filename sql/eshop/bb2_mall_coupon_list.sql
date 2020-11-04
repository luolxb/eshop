create table coupon_list
(
    id           int(8) auto_increment
        primary key,
    cid          int(8)      default 0  not null comment '优惠券 对应coupon表id',
    type         tinyint(1)  default 0  not null comment '发放类型 0下单赠送 1 按用户发放 2 免费领取 3 线下发放',
    uid          int(8)      default 0  not null comment '用户id',
    order_id     int(8)      default 0  not null comment '订单id',
    get_order_id int                    null comment '送券订单ID',
    use_time     int         default 0  not null comment '使用时间',
    code         varchar(10) default '' null comment '优惠券兑换码',
    send_time    int         default 0  not null comment '发放时间',
    store_id     int(10)     default 0  null comment '商家店铺ID',
    status       tinyint     default 0  null comment '0未使用1已使用2已过期',
    deleted      tinyint(1)  default 0  null comment '删除标识;1:删除,0未删除'
)
    charset = utf8;

create index cid
    on coupon_list (cid);

create index code
    on coupon_list (code);

create index order_id
    on coupon_list (order_id);

create index store_id
    on coupon_list (store_id);

create index uid
    on coupon_list (uid);

