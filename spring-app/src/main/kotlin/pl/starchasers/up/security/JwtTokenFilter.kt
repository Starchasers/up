package pl.starchasers.up.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.ROLE_KEY
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

private const val ROLE_PREFIX = "ROLE_"

class JwtTokenFilter(
        private val tokenService: JwtTokenService
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        val token = (request as HttpServletRequest).getHeader("Authorization")

        try {
            val claims = tokenService.parseToken(token.removePrefix("Bearer "))

            val authorities = mutableListOf<GrantedAuthority>()
            authorities.add(SimpleGrantedAuthority(getRoleString(Role.USER)))
            if (claims[ROLE_KEY] == Role.ADMIN) authorities.add(SimpleGrantedAuthority(getRoleString(Role.ADMIN)))


            SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(claims.subject, null, authorities)
        } catch (e: Exception) {
            SecurityContextHolder.clearContext()
        }

        chain.doFilter(request, response)
    }

    private fun getRoleString(role: Role): String = ROLE_PREFIX + role

}