create table goods_consult
(
    id           int auto_increment comment '商品咨询id'
        primary key,
    goods_id     int           default 0  null comment '商品id',
    username     varchar(32)   default '' null comment '网名',
    add_time     int           default 0  null comment '咨询时间',
    consult_type tinyint(1)    default 1  null comment '1 商品咨询 2 支付咨询 3 配送 4 售后',
    content      varchar(1024) default '' null comment '咨询内容',
    parent_id    int           default 0  null comment '父id 用于管理员回复',
    store_id     int           default 0  null comment '店铺id',
    is_show      tinyint(1)    default 0  null comment '是否显示',
    status       tinyint(1)    default 0  null comment '管理回复状态，0未回复，1已回复',
    user_id      int                      null comment '客户id'
)
    charset = utf8;

