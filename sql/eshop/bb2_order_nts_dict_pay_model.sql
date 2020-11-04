create table nts_dict_pay_model
(
    pay_model_id int(5) auto_increment comment '编号'
        primary key,
    name         varchar(100) default '' null comment '支付名称',
    image        varchar(200) default '' null comment '支付图标',
    descr        varchar(500) default '' null comment '支付描述',
    delete_flag  tinyint(1)   default 1  null comment '删除标志 1-正常状态 2-已删除',
    create_time  datetime                null comment '创建时间',
    update_time  datetime                null comment '更新时间'
)
    comment '字典-支付方式表' collate = utf8mb4_bin;

