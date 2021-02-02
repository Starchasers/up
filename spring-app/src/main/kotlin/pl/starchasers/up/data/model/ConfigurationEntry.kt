package pl.starchasers.up.data.model

import javax.persistence.*

@Entity
class ConfigurationEntry(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(name="configuration_key", unique = true, nullable = false)
        @Enumerated(EnumType.STRING)
        val key: ConfigurationKey,

        @Column(name="configuration_value", nullable = false)
        var value: String
)

enum class ConfigurationKey(val defaultValue: String) {
    /**
     * Maximum allowed file size in bytes for anonymous uploads.
     * Default value: 1GB
     */
    ANONYMOUS_MAX_FILE_SIZE("${1L * 1024 * 1024 * 1024}"),

    /**
     * Default time in milliseconds, after which uploaded anonymous file will be deleted
     * Default value: 1 day
     */
    ANONYMOUS_DEFAULT_FILE_LIFETIME("${1L * 24 * 60 * 60 * 1000}"),

    /**
     * Maximum configurable by user time in milliseconds, after which uploaded anonymous file will be deleted.
     * Default value: 1 day
     */
    ANONYMOUS_MAX_FILE_LIFETIME("${1L * 24 * 60 * 60 * 1000}"),

    /**
     * Maximum allowed temporary file size in bytes for logged in users.
     * New users will be initialized with this configuration.
     * Default value: 5GB
     */
    DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE("${5L * 1024 * 1024 * 1024}"),

    /**
     * Maximum allowed file size in bytes for permanent uploads for logged in users.
     * new users will be initialized with this configuration
     * Default value: 512MB
     */
    DEFAULT_USER_MAX_PERMANENT_FILE_SIZE("${512L * 1024 * 1024}"),

    /**
     * Default time in milliseconds, after which file uploaded by a logged in user will be deleted.
     * New users will be initialized with this configuration.
     * Default value: 1 day
     */
    DEFAULT_USER_DEFAULT_FILE_LIFETIME("${1L * 24 * 60 * 60 * 1000}"),

    /**
     * Maximum configurable by user time in milliseconds, after which uploaded file will be deleted.
     * New users will be initialized with this configuration
     * Default value: 0 (no limit)
     */
    DEFAULT_USER_MAX_FILE_LIFETIME("0")
}