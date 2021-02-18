package pl.starchasers.up.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import org.springframework.web.util.WebUtils
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.util.SetCookieHeaderValueBuilder
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenFilter(
    private val tokenService: JwtTokenService
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        try {
            var accessToken =
                WebUtils.getCookie((request as HttpServletRequest), JwtTokenService.ACCESS_TOKEN_COOKIE_NAME)?.value
            val refreshToken = WebUtils.getCookie(request, JwtTokenService.REFRESH_TOKEN_COOKIE_NAME)?.value

            if (accessToken == null && refreshToken != null) {
                accessToken = tokenService.issueAccessToken(refreshToken)
                (response as HttpServletResponse).addHeader(
                    "Set-Cookie",
                    SetCookieHeaderValueBuilder(JwtTokenService.ACCESS_TOKEN_COOKIE_NAME, accessToken)
                        .withMaxAge(JwtTokenService.ACCESS_TOKEN_VALID_TIME)
                        .withPath("/")
                        .httpOnly()
                        .build()
                )
            }

            val claims = tokenService.parseToken(accessToken!!)

            val authorities = mutableListOf<GrantedAuthority>()
            authorities.add(SimpleGrantedAuthority(Role.USER.roleString()))
            if (Role.valueOf(claims[JwtTokenService.ROLE_KEY] as String) == Role.ADMIN) authorities.add(
                SimpleGrantedAuthority(Role.ADMIN.roleString())
            )

            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(claims.subject, null, authorities)
        } catch (e: Exception) {
            SecurityContextHolder.clearContext()
        }

        chain.doFilter(request, response)
    }
}
