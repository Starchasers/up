package pl.starchasers.up.data.model

import com.sun.istack.NotNull
import pl.starchasers.up.data.value.RefreshTokenId
import java.sql.Timestamp
import javax.persistence.*

@Entity
class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @NotNull
    @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY)
    val user: User,

    @Embedded
    val token: RefreshTokenId,

    @Column(nullable = false, updatable = false)
    val creationDate: Timestamp,

    @Column(nullable = false, updatable = false)
    val expirationDate: Timestamp
)
