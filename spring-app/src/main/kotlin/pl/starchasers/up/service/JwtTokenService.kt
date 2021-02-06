package pl.starchasers.up.service

import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.starchasers.up.data.model.RefreshToken
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.RefreshTokenId
import pl.starchasers.up.exception.JwtTokenException
import pl.starchasers.up.repository.RefreshTokenRepository
import pl.starchasers.up.util.Util
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import javax.annotation.PostConstruct

interface JwtTokenService {
    fun issueRefreshToken(user: User): String

    fun refreshRefreshToken(oldRefreshToken: String): String

    fun issueAccessToken(refreshToken: String): String

    fun verifyRefreshToken(token: RefreshTokenId, user: User)

    fun parseToken(token: String): Claims

    fun invalidateUser(user: User)

    fun invalidateRefreshToken(refreshToken: String)

    companion object JwtTokenService {
        const val TOKEN_ID_KEY = "tokenId"
        const val ROLE_KEY = "role"
        const val REFRESH_TOKEN_COOKIE_NAME = "refresh_token"
        const val ACCESS_TOKEN_COOKIE_NAME = "access_token"
        const val REFRESH_TOKEN_VALID_TIME: Long = 7 * 24 * 60 * 60 * 1000
        const val ACCESS_TOKEN_VALID_TIME: Long = 10 * 60 * 1000
    }
}

@Service
class JwtTokenServiceImpl(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userService: UserService
) : JwtTokenService {

    @Value("\${up.jwt-secret}")
    private var secret = ""

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    private fun generateSecret() {
        if (secret.isBlank()) {
            logger.info("JWT secret not defined. Generating random secret...")
            secret = Util().secureAlphanumericRandomString(64)
        }
    }

    override fun issueRefreshToken(user: User): String {
        val claims = Jwts.claims().setSubject(user.id.toString())
        val now = Date()
        val tokenId = RefreshTokenId(UUID.randomUUID().toString())

        claims[JwtTokenService.TOKEN_ID_KEY] = tokenId.value
        val refreshToken = RefreshToken(
            0,
            user,
            tokenId,
            Timestamp.valueOf(LocalDateTime.now()),
            Timestamp.valueOf(LocalDateTime.now().plusNanos(JwtTokenService.REFRESH_TOKEN_VALID_TIME * 1000))
        )

        refreshTokenRepository.save(refreshToken)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + JwtTokenService.REFRESH_TOKEN_VALID_TIME))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }

    override fun refreshRefreshToken(oldRefreshToken: String): String {
        val oldClaims = parseToken(oldRefreshToken)
        val user = userService.getUser(oldClaims.subject.toLong())

        verifyRefreshToken(oldClaims.getTokenId(), user)

        return issueRefreshToken(user)
    }

    override fun issueAccessToken(refreshToken: String): String {
        val refreshTokenClaims = parseToken(refreshToken)
        val user = userService.getUser(refreshTokenClaims.subject.toLong())

        val claims = Jwts.claims().setSubject(user.id.toString())
        claims[JwtTokenService.ROLE_KEY] = user.role

        verifyRefreshToken(RefreshTokenId(refreshTokenClaims[JwtTokenService.TOKEN_ID_KEY] as String), user)

        val now = Date()

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + JwtTokenService.ACCESS_TOKEN_VALID_TIME))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }

    override fun verifyRefreshToken(token: RefreshTokenId, user: User) {
        refreshTokenRepository.findFirstByTokenAndUser(token, user) ?: throw JwtTokenException("Invalid refresh token.")
    }

    override fun parseToken(token: String): Claims {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
        } catch (e: ExpiredJwtException) {
            throw JwtException("Token expired")
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
        refreshTokenRepository.deleteAllByToken(claims.getTokenId())
    }
}

fun Claims.getTokenId(): RefreshTokenId {
    val idString = this[JwtTokenService.TOKEN_ID_KEY]
    if (idString !is String) throw JwtTokenException("Invalid refresh token")
    return RefreshTokenId(idString)
}
