create table wx_user
(
    id                       int auto_increment comment '表id'
        primary key,
    uid                      int                     not null comment 'uid',
    wxname                   varchar(60)  default '' not null comment '公众号名称',
    aeskey                   varchar(256) default '' not null comment 'aeskey',
    encode                   tinyint(1)   default 0  not null comment 'encode',
    appid                    varchar(50)  default '' not null comment 'appid',
    appsecret                varchar(50)  default '' not null comment 'appsecret',
    wxid                     varchar(64)  default '' not null comment '公众号原始ID',
    weixin                   char(64)                not null comment '微信号',
    headerpic                char(255)               not null comment '头像地址',
    token                    char(255)               not null comment 'token',
    mutual_token             varchar(150) default '' not null comment '交互token 与微信',
    create_time              int                     not null comment 'create_time',
    updatetime               int                     not null comment 'updatetime',
    tplcontentid             varchar(2)   default '' not null comment '内容模版ID',
    share_ticket             varchar(150) default '' not null comment '分享ticket',
    share_dated              char(15)                not null comment 'share_dated',
    authorizer_access_token  varchar(200) default '' not null comment 'authorizer_access_token',
    authorizer_refresh_token varchar(200) default '' not null comment 'authorizer_refresh_token',
    authorizer_expires       char(10)                not null comment 'authorizer_expires',
    type                     tinyint(1)   default 0  not null comment '类型',
    web_access_token         varchar(200) default '' null comment ' 网页授权token',
    web_refresh_token        varchar(200) default '' null comment 'web_refresh_token',
    web_expires              int                     not null comment '过期时间',
    qr                       varchar(200) default '' not null comment 'qr',
    menu_config              text                    null comment '菜单',
    wait_access              tinyint(1)   default 0  null comment '微信接入状态,0待接入1已接入',
    host                     varchar(255) default '' null comment '服务器域名',
    apiurl                   varchar(255) default '' null comment '服务器地址URL'
)
    comment '微信公共帐号' charset = utf8;

create index uid
    on wx_user (uid);

create index uid_2
    on wx_user (uid);

