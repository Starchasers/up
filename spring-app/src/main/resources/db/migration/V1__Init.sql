START TRANSACTION;

CREATE TABLE `configuration_entry` (
                                       `id` bigint(20) NOT NULL,
                                       `configuration_key` varchar(255) NOT NULL,
                                       `configuration_value` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `file_entry` (
                              `id` bigint(20) NOT NULL,
                              `file_access_token` varchar(128) DEFAULT NULL,
                              `content_type` varchar(256) NOT NULL,
                              `created_date` datetime(6) NOT NULL,
                              `encrypted` bit(1) NOT NULL,
                              `filename` varchar(1024) NOT NULL,
                              `file_key` varchar(32) NOT NULL,
                              `file_password` varchar(255) DEFAULT NULL,
                              `permanent` bit(1) NOT NULL,
                              `file_size` bigint(20) DEFAULT NULL,
                              `to_delete_date` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `refresh_token` (
                                 `id` bigint(20) NOT NULL,
                                 `creation_date` datetime(6) NOT NULL,
                                 `expiration_date` datetime(6) NOT NULL,
                                 `refresh_token` varchar(255) NOT NULL,
                                 `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user` (
                        `id` bigint(20) NOT NULL,
                        `default_file_lifetime` bigint(20) DEFAULT NULL,
                        `email` varchar(64) DEFAULT NULL,
                        `max_file_lifetime` bigint(20) DEFAULT NULL,
                        `max_permanent_file_size` bigint(20) DEFAULT NULL,
                        `max_temporary_file_size` bigint(20) DEFAULT NULL,
                        `password` varchar(160) NOT NULL,
                        `role` int(11) NOT NULL,
                        `username` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `configuration_entry`
    ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_o5qbigl9owijqeeps9on1r19k` (`configuration_key`);

ALTER TABLE `file_entry`
    ADD PRIMARY KEY (`id`);

ALTER TABLE `refresh_token`
    ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_mpvu2fmreqp9cqddttyfaym9v` (`refresh_token`),
  ADD KEY `FKfgk1klcib7i15utalmcqo7krt` (`user_id`);

ALTER TABLE `user`
    ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`);

ALTER TABLE `configuration_entry`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

ALTER TABLE `file_entry`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `refresh_token`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `user`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

ALTER TABLE `refresh_token`
    ADD CONSTRAINT `FKfgk1klcib7i15utalmcqo7krt` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
COMMIT;

