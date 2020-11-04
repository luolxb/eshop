create table team_activity
(
    team_id      int(10) auto_increment
        primary key,
    act_name     varchar(255)         default ''   null comment '拼团活动标题',
    team_type    tinyint(1)           default 0    null comment '拼团活动类型,0分享团1佣金团2抽奖团',
    time_limit   int                  default 0    null comment '成团有效期。单位（秒)',
    team_price   decimal(10, 2)       default 0.00 null comment '拼团价',
    needer       int(10)              default 2    null comment '需要成团人数',
    goods_name   varchar(255)         default ''   null comment '商品名称',
    bonus        decimal(10, 2)       default 0.00 null comment '团长佣金',
    stock_limit  int                  default 0    null comment '抽奖限量',
    goods_id     int unsigned                      null comment '商品id',
    buy_limit    smallint(4)          default 0    null comment '单次团购买限制数0为不限制',
    sales_sum    int unsigned         default 0    null comment '已拼多少件',
    virtual_num  int(10)              default 0    null comment '虚拟销售基数',
    share_title  varchar(100)                      null comment '分享标题',
    share_desc   varchar(255)                      null comment '分享描述',
    share_img    varchar(150)                      null comment '分享图片',
    store_id     int                  default 0    null comment '商家id',
    sort         smallint(4) unsigned default 0    not null comment '排序',
    is_recommend tinyint(1) unsigned  default 0    not null comment '是否推荐',
    status       tinyint(1)           default 0    null comment '0待审核1正常2拒绝3关闭',
    is_lottery   tinyint(1)           default 0    null comment '是否已经抽奖.1是，0否',
    add_time     int(15)              default 0    null comment '创建时间',
    deleted      tinyint(1)           default 0    not null comment '软删除',
    item_id      int(10)              default 0    null comment '商品规格id'
)
    comment '拼团活动表' charset = utf8;

