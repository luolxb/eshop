create table team_goods_item
(
    team_id    int unsigned         not null comment '活动id',
    goods_id   int unsigned         not null comment '商品id',
    item_id    int unsigned         not null comment '规格id',
    team_price decimal(10, 2)       not null comment '拼团价',
    sales_sum  int        default 0 not null comment '已拼数量',
    deleted    tinyint(1) default 0 not null comment '是否已删除0否，1删除'
)
    engine = MyISAM
    charset = utf8;

