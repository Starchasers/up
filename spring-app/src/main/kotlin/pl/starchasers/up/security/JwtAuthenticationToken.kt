package pl.starchasers.up.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(private val principal: Any, private val credentials: Any?, private val refreshTokenString: String?, authorities: Collection<GrantedAuthority>?) : AbstractAuthenticationToken(authorities) {

    init {
        isAuthenticated = true
    }

    fun getRefreshTokenString(): String? = refreshTokenString

    override fun getCredentials(): Any? = credentials

    override fun getPrincipal(): Any = principal
}
