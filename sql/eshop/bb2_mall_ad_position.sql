create table ad_position
(
    position_id    int(3) unsigned auto_increment
        primary key,
    position_name  varchar(60)       default '' not null comment '广告位置名称',
    ad_width       smallint unsigned default 0  not null comment '广告位宽度',
    ad_height      smallint unsigned default 0  not null comment '广告位高度',
    position_desc  varchar(255)      default '' not null comment '广告描述',
    position_style text                         null comment '模板',
    is_open        tinyint(1)        default 0  null comment '0关闭1开启'
)
    charset = utf8;

