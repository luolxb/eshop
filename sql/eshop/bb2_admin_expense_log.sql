create table expense_log
(
    id          int unsigned auto_increment
        primary key,
    admin_id    int                  null comment '操作管理员',
    money       decimal(10, 2)       null comment '支出金额',
    integral    int(10)    default 0 null comment '赠送积分',
    type        tinyint(1) default 0 null comment '支出类型0商家提现1用户提现2订单取消退款3订单售后退款4其他',
    addtime     int                  null comment '日志记录时间',
    log_type_id int        default 0 null comment '业务关联ID',
    user_id     int(10)    default 0 null comment '涉及会员id',
    store_id    int(10)    default 0 null comment '涉及商家id'
)
    comment '平台支出金额或赠送积分记录日志' charset = latin1;

