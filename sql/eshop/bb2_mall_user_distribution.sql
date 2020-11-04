create table user_distribution
(
    user_id    int                    null comment '分销会员id',
    user_name  varchar(50)            null comment '会员昵称',
    goods_id   int                    null comment '商品id',
    goods_name varchar(150)           null comment '商品名称',
    cat_id     smallint     default 0 null comment '商品分类ID',
    brand_id   mediumint(8) default 0 null comment '商品品牌',
    share_num  int(10)      default 0 null comment '分享次数',
    sales_num  int          default 0 null comment '分销销量',
    id         int(11) unsigned auto_increment
        primary key,
    addtime    int                    null comment '加入个人分销库时间',
    store_id   int                    not null comment '商品对应的店铺ID'
)
    comment '用户选择分销商品表' charset = utf8;

create index goods_id
    on user_distribution (goods_id);

create index user_id
    on user_distribution (user_id);

