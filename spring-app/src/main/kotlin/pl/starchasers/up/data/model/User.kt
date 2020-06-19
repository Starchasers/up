package pl.starchasers.up.data.model

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
        var role: Role
)