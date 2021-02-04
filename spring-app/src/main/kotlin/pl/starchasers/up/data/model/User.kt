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

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "maxTemporaryFileSize"))
    var maxTemporaryFileSize: FileSize = FileSize(0),

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "maxPermanentFileSize"))
    var maxPermanentFileSize: FileSize = FileSize(0),

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "defaultFileLifetime"))
    var defaultFileLifetime: Milliseconds = Milliseconds(0),

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "maxFileLifetime"))
    var maxFileLifetime: Milliseconds = Milliseconds(0),

    @OneToMany(fetch = FetchType.LAZY)
    val files: MutableSet<FileEntry> = mutableSetOf()
)
