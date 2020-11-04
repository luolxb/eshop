create table account_log_store
(
    log_id        int(8) unsigned auto_increment
        primary key,
    store_id      int(8) unsigned         not null,
    store_money   decimal(10, 2)          not null comment '店铺金额',
    pending_money decimal(10, 2)          not null comment '店铺未结算金额',
    change_time   int unsigned            not null comment '变动时间',
    `desc`        varchar(255) default '' not null comment '描述',
    order_sn      varchar(50)             null comment '订单编号',
    order_id      int(10)                 null comment '订单id'
)
    charset = utf8;

create index user_id
    on account_log_store (store_id);

