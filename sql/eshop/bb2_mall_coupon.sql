create table coupon
(
    id              int(8) auto_increment comment '表id'
        primary key,
    name            varchar(50)    default ''   not null comment '优惠券名字',
    type            tinyint(1)     default 0    not null comment '发放类型 0下单赠送 1 按用户发放 2 免费领取 3 线下发放',
    use_type        tinyint(1)     default 0    null comment '使用范围：0全店通用1指定商品可用2指定分类商品可用',
    money           decimal(10, 2) default 0.00 not null comment '优惠券金额',
    `condition`     decimal(10, 2) default 0.00 not null comment '使用条件',
    createnum       int            default 10   null comment '发放数量',
    send_num        int            default 0    null comment '已领取数量',
    use_num         int            default 0    null comment '已使用数量',
    send_start_time int                         null comment '发放开始时间',
    send_end_time   int                         null comment '发放结束时间',
    use_start_time  int                         null comment '使用开始时间',
    use_end_time    int                         null comment '使用结束时间',
    add_time        int                         null comment '添加时间',
    store_id        int(10)        default 0    null comment '商家店铺ID',
    status          tinyint(1)     default 1    null comment '状态：1有效2无效',
    coupon_info     varchar(255)                null comment '优惠券描述',
    validity_day    int(1)         default 0    null comment '使用有效期x天，仅用于新人优惠券'
)
    comment '优惠券表' charset = utf8;

create index store_id
    on coupon (store_id);

create index use_end_time
    on coupon (use_end_time);

