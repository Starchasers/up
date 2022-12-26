package pl.starchasers.up.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties("up.jwt")
@Validated
data class JwtProperties constructor(
    val base64Secret: String = ""
)
