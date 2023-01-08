package pl.starchasers.up.data.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import pl.starchasers.up.data.value.RefreshTokenId
import java.sql.Timestamp

@Table(name = "refresh_token", schema = "public")
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
