create table branch_table
(
    branch_id         bigint        not null
        primary key,
    xid               varchar(128)  not null,
    transaction_id    bigint        null,
    resource_group_id varchar(32)   null,
    resource_id       varchar(256)  null,
    branch_type       varchar(8)    null,
    status            tinyint       null,
    client_id         varchar(64)   null,
    application_data  varchar(2000) null,
    gmt_create        datetime(6)   null,
    gmt_modified      datetime(6)   null
)
    charset = utf8;

create index idx_xid
    on branch_table (xid);

INSERT INTO bb2_seata.branch_table (branch_id, xid, transaction_id, resource_group_id, resource_id, branch_type, status, client_id, application_data, gmt_create, gmt_modified) VALUES (35391347137454080, '192.168.1.80:8091:35391343253528576', 35391343253528576, null, 'jdbc:mysql://192.168.1.80:3306/bb2_order', 'AT', 0, 'order:172.18.0.1:51104', null, '2020-08-08 15:52:35.927372', '2020-08-08 15:52:35.927372');
INSERT INTO bb2_seata.branch_table (branch_id, xid, transaction_id, resource_group_id, resource_id, branch_type, status, client_id, application_data, gmt_create, gmt_modified) VALUES (36406822768222208, '192.168.1.80:8091:36406816430628864', 36406816430628864, null, 'jdbc:mysql://192.168.1.80:3306/bb2_order', 'AT', 0, 'order:172.18.0.1:39426', null, '2020-08-11 11:07:44.176698', '2020-08-11 11:07:44.176698');
INSERT INTO bb2_seata.branch_table (branch_id, xid, transaction_id, resource_group_id, resource_id, branch_type, status, client_id, application_data, gmt_create, gmt_modified) VALUES (36406827893661697, '192.168.1.80:8091:36406815163949056', 36406815163949056, null, 'jdbc:mysql://192.168.1.80:3306/bb2_order', 'AT', 0, 'order:172.18.0.1:39426', null, '2020-08-11 11:07:44.595721', '2020-08-11 11:07:44.595721');
INSERT INTO bb2_seata.branch_table (branch_id, xid, transaction_id, resource_group_id, resource_id, branch_type, status, client_id, application_data, gmt_create, gmt_modified) VALUES (36412233894338561, '192.168.1.80:8091:36412227556745216', 36412227556745216, null, 'jdbc:mysql://192.168.1.80:3306/bb2_order', 'AT', 0, 'order:172.18.0.1:41488', null, '2020-08-11 11:29:13.467630', '2020-08-11 11:29:13.467630');
INSERT INTO bb2_seata.branch_table (branch_id, xid, transaction_id, resource_group_id, resource_id, branch_type, status, client_id, application_data, gmt_create, gmt_modified) VALUES (36535964717293569, '192.168.1.80:8091:36535948267233280', 36535948267233280, null, 'jdbc:mysql://192.168.1.80:3306/bb2_order', 'AT', 0, 'order:172.18.0.1:43540', null, '2020-08-11 19:40:54.922999', '2020-08-11 19:40:54.922999');
INSERT INTO bb2_seata.branch_table (branch_id, xid, transaction_id, resource_group_id, resource_id, branch_type, status, client_id, application_data, gmt_create, gmt_modified) VALUES (36536138508279809, '192.168.1.80:8091:36536124348309504', 36536124348309504, null, 'jdbc:mysql://192.168.1.80:3306/bb2_order', 'AT', 0, 'order:172.18.0.1:43540', null, '2020-08-11 19:41:34.683198', '2020-08-11 19:41:34.683198');
INSERT INTO bb2_seata.branch_table (branch_id, xid, transaction_id, resource_group_id, resource_id, branch_type, status, client_id, application_data, gmt_create, gmt_modified) VALUES (36767372815638529, '192.168.1.80:8091:36767364330561536', 36767364330561536, null, 'jdbc:mysql://192.168.1.80:3306/bb2_order', 'AT', 0, 'order:172.18.0.1:43540', null, '2020-08-12 11:00:25.870649', '2020-08-12 11:00:25.870649');
INSERT INTO bb2_seata.branch_table (branch_id, xid, transaction_id, resource_group_id, resource_id, branch_type, status, client_id, application_data, gmt_create, gmt_modified) VALUES (37938617355608064, '192.168.1.80:8091:37938609503870976', 37938609503870976, null, 'jdbc:mysql://192.168.1.80:3306/bb2_order', 'AT', 0, 'order:172.18.0.1:50710', null, '2020-08-15 16:34:32.305056', '2020-08-15 16:34:32.305056');
INSERT INTO bb2_seata.branch_table (branch_id, xid, transaction_id, resource_group_id, resource_id, branch_type, status, client_id, application_data, gmt_create, gmt_modified) VALUES (39061959773003776, '192.168.1.80:8091:39061951128543232', 39061951128543232, null, 'jdbc:mysql://192.168.1.80:3306/bb2_order', 'AT', 0, 'order:172.18.0.1:37530', null, '2020-08-18 18:58:18.267818', '2020-08-18 18:58:18.267818');
INSERT INTO bb2_seata.branch_table (branch_id, xid, transaction_id, resource_group_id, resource_id, branch_type, status, client_id, application_data, gmt_create, gmt_modified) VALUES (39774199283195904, '192.168.1.80:8091:39774196171022336', 39774196171022336, null, 'jdbc:mysql://192.168.1.80:3306/bb2_order', 'AT', 0, 'order:172.18.0.1:41588', null, '2020-08-20 18:08:29.680142', '2020-08-20 18:08:29.680142');
INSERT INTO bb2_seata.branch_table (branch_id, xid, transaction_id, resource_group_id, resource_id, branch_type, status, client_id, application_data, gmt_create, gmt_modified) VALUES (39784386303893505, '192.168.1.80:8091:39784382906507264', 39784382906507264, null, 'jdbc:mysql://192.168.1.80:3306/bb2_order', 'AT', 0, 'order:172.18.0.1:38162', null, '2020-08-20 18:48:57.538787', '2020-08-20 18:48:57.538787');