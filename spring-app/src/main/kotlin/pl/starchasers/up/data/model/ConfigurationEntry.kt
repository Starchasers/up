package pl.starchasers.up.data.model

import javax.persistence.*

@Entity
class ConfigurationEntry(
        @Id
        @Enumerated(EnumType.STRING)
        val id: Configuration,

        @Column(nullable = false)
        val value: String
)

enum class Configuration {
    MAX_ANONYMOUS_FILE_SIZE,

}