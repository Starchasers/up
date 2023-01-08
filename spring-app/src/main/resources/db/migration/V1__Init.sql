start transaction;

create table "configuration_entry"
(
    "id"                  serial primary key,
    "configuration_key"   varchar(255) not null,
    "configuration_value" varchar(255) not null,
    constraint uq__configuration_entry__configuration_key unique (configuration_key)
);

create table "user"
(
    "id"                      serial primary key,
    "default_file_lifetime"   bigint      default null,
    "email"                   varchar(64) default null,
    "max_file_lifetime"       bigint      default null,
    "max_permanent_file_size" bigint      default null,
    "max_temporary_file_size" bigint      default null,
    "password"                varchar(160) not null,
    "role"                    smallint     not null,
    "username"                varchar(32)  not null
);

create table "file_entry"
(
    "id"                serial primary key,
    "file_access_token" varchar(128) default null,
    "content_type"      varchar(256)  not null,
    "created_date"      timestamptz   not null,
    "encrypted"         boolean       not null,
    "filename"          varchar(1024) not null,
    "file_key"          varchar(32)   not null,
    "file_password"     varchar(255) default null,
    "permanent"         boolean       not null,
    "file_size"         bigint       default null,
    "to_delete_date"    timestamptz  default null,
    "owner_id"          bigint       default null,
    constraint fk__file_entry__owner_id foreign key (owner_id) references "user" (id)
);
commit;

create index ix__file_entry__owner_id on file_entry (owner_id);

create table "refresh_token"
(
    "id"              serial primary key,
    "creation_date"   timestamptz  not null,
    "expiration_date" timestamptz  not null,
    "refresh_token"   varchar(255) not null,
    "user_id"         bigint default null,
    constraint uq__refresh_token__refresh_token unique (refresh_token),
    constraint fk__refresh_token__user_id foreign key (user_id) references "user" (id)
);

create index ix__refresh_token__user_id on refresh_token (user_id);

create unique index ux__user__username on "user" (username);

commit;
