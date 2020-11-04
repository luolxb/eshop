create table prom_goods
(
    id           bigint auto_increment comment '活动ID'
        primary key,
    title        varchar(60)  default ''                not null comment '促销活动名称',
    type         int(2)       default 0                 not null comment '促销类型:0直接打折,1减价优惠,2固定金额出售,3买就赠优惠券',
    expression   varchar(100) default ''                not null comment '优惠体现',
    description  text                                   null comment '活动描述',
    start_time   int                                    not null comment '活动开始时间',
    end_time     int                                    not null comment '活动结束时间',
    status       tinyint(1)   default 1                 null comment '1正常，0管理员关闭',
    is_end       tinyint(1)   default 0                 null comment '是否已结束',
    `group`      varchar(255)                           null comment '适用范围',
    store_id     int(10)      default 0                 null comment '商家店铺id',
    orderby      int(10)      default 0                 null comment '排序',
    prom_img     varchar(150)                           null comment '活动宣传图片',
    recommend    tinyint(1)   default 0                 null comment '是否推荐',
    buy_limit    int(10)                                null comment '每人限购数',
    is_deleted   tinyint(1)   default 0                 not null comment '软删除',
    gmt_create   datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    gmt_modified datetime     default CURRENT_TIMESTAMP null comment '更新时间'
)
    charset = utf8;

