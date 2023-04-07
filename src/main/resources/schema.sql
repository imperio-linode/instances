drop table if exists auth_user;
drop table if exists instance;
drop table if exists instance_address;
drop table if exists instance_alert;
drop table if exists instance_spec;

create table auth_user
(
    user_id         bigint not null primary key,
    user_first_name varchar(15),
    user_sure_name  varchar(20),
    user_name       varchar(15),
    user_password   varchar(25),
    user_email      varchar(35),
    user_isActive   bool
);

create table instance
(
    instance_id                     serial
        constraint id primary key,
    instance_region                 varchar(5),
    instance_alert_id               bigint,
    instance_address_id             bigint,
    instance_specs_id               bigint,
    instance_backup_available       bool,
    instance_backup_enabled         bool,
    instance_backup_last_successful timestamp,
    instance_backup_day             int,
    instance_backup_window          varchar(35),
    instance_created                varchar(35),
    instance_group                  varchar(35),
    instance_host_uuid              varchar(35),
    instance_hypervisor             varchar(35),
    instance_image                  varchar(35),
    instance_label                  varchar(35),
    instance_status                 varchar(35),
    instance_tags                   varchar(35)[],
    instance_type                   varchar(35),
    instance_updated                varchar(35),
    instance_watchdog_enable        bool
);

create table instance_address
(
    i_ip_id bigint not null primary key,
    i_ip_v4 inet[1],
    i_ip_v6 inet
);

create table instance_alert
(
    i_alert_id             bigint not null primary key,
    i_alert_cpu            integer,
    i_alert_io             integer,
    i_alert_network_in     integer,
    i_alert_network_out    integer,
    i_alert_transfer_quota integer
);

create table instance_spec
(
    i_spec_id       bigint not null primary key,
    i_spec_disk     integer,
    i_spec_memory   integer,
    i_spec_vcpu     integer,
    i_spec_gpus     integer,
    i_spec_transfer integer

);

insert into auth_user (user_id, user_first_name, user_sure_name, user_name, user_password, user_email, user_isactive)
values (1, 'Michal', 'Cop', 'mcop', 'mcop', 'michalcop@bntech.dev', true);
insert into auth_user (user_id, user_first_name, user_sure_name, user_name, user_password, user_email, user_isactive)
values (2, 'Test', 'User', 'user', 'pass', 'poownijmy@gmail.com', true);

insert into instance (instance_id, instance_alert_id, instance_address_id, instance_specs_id,
                      instance_backup_available,
                      instance_backup_enabled, instance_backup_last_successful, instance_backup_day,
                      instance_backup_window, instance_created, instance_group, instance_host_uuid, instance_hypervisor,
                      instance_image, instance_label, instance_status, instance_tags, instance_type, instance_updated,
                      instance_watchdog_enable)
values (0, 1, 1, 1, false, false, '2004-10-19 10:23:54', 3, 'window', 'creted', 'group', 'host_uuid',
        'hypervisor', 'image', 'label',
        'running', '{tags, tags2}', 'type', 'updated', false);

insert into instance_address(i_ip_id, i_ip_v4, i_ip_v6)
values (1, '{192.168.0.1, 192.168.0.2}', '0000:0000:0000:0000:0000:ffff:c0a8:0001');

insert into instance_alert (i_alert_id, i_alert_cpu, i_alert_io, i_alert_network_in, i_alert_network_out,
                            i_alert_transfer_quota)
values (1, 30, 30, 30, 30, 30);

insert into instance_spec (i_spec_id, i_spec_disk, i_spec_memory, i_spec_transfer, i_spec_vcpu, i_spec_gpus)
values (1, 2137, 420, 10, 10, 3);
