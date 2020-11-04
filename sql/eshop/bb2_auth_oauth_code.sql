create table oauth_code
(
    code           varchar(128) null,
    authentication blob         null
)
    engine = MyISAM
    charset = utf8;

