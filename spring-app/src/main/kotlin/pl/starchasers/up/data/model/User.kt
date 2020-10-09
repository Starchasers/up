package pl.starchasers.up.data.model

import pl.starchasers.up.data.value.FileSize
import pl.starchasers.up.data.value.FileSizeConverter
import pl.starchasers.up.data.value.Milliseconds
import pl.starchasers.up.data.value.MillisecondsConverter
import pl.starchasers.up.security.Role
import javax.persistence.*

@Entity
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(length = 32, unique = true, updatable = true, nullable = false)
        val username: String,

        @Column(length = 160, nullable = false)
        var password: String,

        @Column(length = 64, nullable = true)
        var email: String?,

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