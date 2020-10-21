package pl.starchasers.up.data.model

import pl.starchasers.up.data.value.*
import pl.starchasers.up.security.Role
import javax.persistence.*

@Entity
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Embedded
        var username: Username,

        @Embedded
        var password: UserPassword,

        @Embedded
        var email: Email?,

        @Column(nullable = false, unique = false)
        var role: Role,

        @Convert(converter = FileSizeConverter::class)
        @Column(nullable = false, unique = false)
        var maxTemporaryFileSize: FileSize =  FileSize(0),

        @Convert(converter = FileSizeConverter::class)
        @Column(nullable = false, unique = false)
        var maxPermanentFileSize: FileSize = FileSize(0),

        @Convert(converter = MillisecondsConverter::class)
        @Column(nullable = false, unique = false)
        var defaultFileLifetime: Milliseconds = Milliseconds(0),

        @Convert(converter = MillisecondsConverter::class)
        @Column(nullable = false, unique = false)
        var maxFileLifetime: Milliseconds = Milliseconds(0)
)