package pl.starchasers.up.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.starchasers.up.data.model.RefreshToken
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.RefreshTokenId

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findFirstByTokenAndUser(token: RefreshTokenId, user: User): RefreshToken?

    fun findAllByUser(user: User): List<RefreshToken>

    fun deleteAllByUser(user: User)

    fun deleteAllByToken(token: RefreshTokenId)
}