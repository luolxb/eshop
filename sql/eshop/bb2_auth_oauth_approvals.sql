create table oauth_approvals
(
    userId         varchar(128)                        null,
    clientId       varchar(128)                        null,
    scope          varchar(128)                        null,
    status         varchar(10)                         null,
    expiresAt      timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    lastModifiedAt timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    engine = MyISAM
    charset = utf8;

