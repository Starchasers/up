package pl.starchasers.up.data.dto.configuration

data class UpdateUserConfigurationDTO(
        /**
         * Maximum allowed temporary file size, in bytes
         */
        val maxTemporaryFileSize: Long,
        /**
         * Maximum allowed time, after which temporary file will be deleted
         */
        val maxFileLifetime: Long,
        /**
         * Default time, after which temporary file will be deleted, if not specified
         */
        val defaultFileLifetime: Long,
        /**
         * Maximum allowed permanent file size, in bytes
         */
        val maxPermanentFileSize: Long
)