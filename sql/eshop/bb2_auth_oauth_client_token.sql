create table oauth_client_token
(
    token_id          varchar(128) null,
    token             blob         null,
    authentication_id varchar(128) not null
        primary key,
    user_name         varchar(128) null,
    client_id         varchar(128) null
)
    engine = MyISAM
    charset = utf8;

