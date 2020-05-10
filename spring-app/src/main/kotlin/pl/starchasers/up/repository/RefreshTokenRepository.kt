package pl.starchasers.up.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.starchasers.up.data.model.RefreshToken
import pl.starchasers.up.data.model.User

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findFirstByTokenAndUser(token: String, user: User): RefreshToken?

    fun deleteAllByUser(user: User)

    fun deleteAllByToken(token: String)
}