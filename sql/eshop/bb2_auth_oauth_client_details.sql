create table oauth_client_details
(
    client_id               varchar(128)  not null
        primary key,
    resource_ids            varchar(128)  null,
    client_secret           varchar(128)  null,
    scope                   varchar(128)  null,
    authorized_grant_types  varchar(128)  null,
    web_server_redirect_uri varchar(128)  null,
    authorities             varchar(128)  null,
    access_token_validity   int           null,
    refresh_token_validity  int           null,
    additional_information  varchar(4096) null,
    autoapprove             varchar(128)  null
)
    engine = MyISAM
    charset = utf8;

INSERT INTO bb2_auth.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('admin', '', '{bcrypt}$2a$10$HBX6q6TndkgMxhSEdoFqWOUtctaJEMoXe49NWh8Owc.4MTunv.wXa', 'server', 'password,refresh_token,sms,third_oauth', null, 'oauth2', null, null, null, null);
INSERT INTO bb2_auth.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('client_1', null, '{bcrypt}$2a$10$HBX6q6TndkgMxhSEdoFqWOUtctaJEMoXe49NWh8Owc.4MTunv.wXa', 'select', 'client_credentials,refresh_token,sms,third_oauth', null, 'oauth2', null, null, null, null);
INSERT INTO bb2_auth.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('seller', null, '{bcrypt}$2a$10$HBX6q6TndkgMxhSEdoFqWOUtctaJEMoXe49NWh8Owc.4MTunv.wXa', 'server', 'password,refresh_token,sms,third_oauth', null, 'oauth2', null, null, null, null);
INSERT INTO bb2_auth.oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('shop', null, '{bcrypt}$2a$10$HBX6q6TndkgMxhSEdoFqWOUtctaJEMoXe49NWh8Owc.4MTunv.wXa', 'server', 'password,refresh_token,sms,third_oauth', null, 'oauth2', null, null, null, null);