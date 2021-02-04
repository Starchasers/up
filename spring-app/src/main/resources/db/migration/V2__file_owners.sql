start transaction;

alter table up.file_entry
    add column owner_id bigint(20) default null,
    add key `ix_file_entry__owner_id` (owner_id),
    add constraint `fk_user__file_entry` foreign key (owner_id) references `user` (`id`);

commit;
