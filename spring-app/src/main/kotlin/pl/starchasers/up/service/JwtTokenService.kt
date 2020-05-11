package pl.starchasers.up.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.starchasers.up.data.model.RefreshToken
import pl.starchasers.up.data.model.User
import pl.starchasers.up.exception.JwtTokenException
import pl.starchasers.up.repository.RefreshTokenRepository
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

const val TOKEN_ID_KEY = "tokenId"
const val ROLE_KEY = "role"

interface JwtTokenService {
    fun issueRefreshToken(user: User): String

    fun refreshRefreshToken(oldRefreshToken: String): String

    fun issueAccessToken(refreshToken: String): String

    fun verifyRefreshToken(token: String, user: User)

    fun parseToken(token: String): Claims

    fun invalidateUser(user: User)

    fun invalidateRefreshToken(refreshToken: String)
}

@Service
class JwtTokenServiceImpl(
        private val refreshTokenRepository: RefreshTokenRepository,
        private val userService: UserService
) : JwtTokenService {

    @Value("\${up.jwt-secret}")
    private val secret = ""

    private val refreshTokenValidTime: Long = 7 * 24 * 60 * 60 * 1000
    private val accessTokenValidTime: Long = 15 * 60 * 1000

    override fun issueRefreshToken(user: User): String {
        val claims = Jwts.claims().setSubject(user.id.toString())
        val now = Date()
        val tokenId = UUID.randomUUID().toString()

        claims[TOKEN_ID_KEY] = tokenId
        val refreshToken = RefreshToken(
                0,
                user,
                tokenId,
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().plusNanos(refreshTokenValidTime * 1000))
        )

        refreshTokenRepository.save(refreshToken)

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date(now.time + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact()
    }

    override fun refreshRefreshToken(oldRefreshToken: String): String {
        val oldClaims = parseToken(oldRefreshToken)
        val user = userService.getUser(oldClaims.subject.toLong())

        verifyRefreshToken(oldClaims[TOKEN_ID_KEY] as String, user)

        return issueRefreshToken(user)
    }


    override fun issueAccessToken(refreshToken: String): String {
        val refreshTokenClaims = parseToken(refreshToken)
        val user = userService.getUser(refreshTokenClaims.subject.toLong())

        val claims = Jwts.claims().setSubject(user.id.toString())
        claims[ROLE_KEY] = user.role

        verifyRefreshToken(refreshTokenClaims[TOKEN_ID_KEY] as String, user)

        val now = Date()

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(Date(now.time + accessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact()

    }

    override fun verifyRefreshToken(token: String, user: User) {
        refreshTokenRepository.findFirstByTokenAndUser(token, user) ?: throw JwtTokenException("Invalid refresh token.")
    }

    override fun parseToken(token: String): Claims {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
        } catch (e: Exception) {
            throw JwtTokenException("Invalid or corrupted token.")
        }
    }

    @Transactional
    override fun invalidateUser(user: User) {
        refreshTokenRepository.deleteAllByUser(user)
    }

    @Transactional
    override fun invalidateRefreshToken(refreshToken: String) {
        val claims = parseToken(refreshToken)
        refreshTokenRepository.deleteAllByToken(claims[TOKEN_ID_KEY] as String)
    }
}