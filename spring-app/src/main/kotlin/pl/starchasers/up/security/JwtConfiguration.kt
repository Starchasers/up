package pl.starchasers.up.security

import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.SecureRandom
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Configuration
class JwtConfiguration {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun jwtSigningKey(jwtProperties: JwtProperties): JwtSigningKey {
        val secretBytes = if (jwtProperties.base64Secret.isNotBlank()) {
            Base64.getDecoder().decode(jwtProperties.base64Secret)
        } else {
            logger.info("JWT secret not defined. Generating random secret...")
            ByteArray(256)
                .apply { SecureRandom().nextBytes(this) }
        }
        return JwtSigningKey(SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.jcaName))
    }
}

data class JwtSigningKey(
    val key: SecretKey
)
