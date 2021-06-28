package pl.starchasers.up.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import org.springframework.web.util.WebUtils
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.util.addCookie
import pl.starchasers.up.util.getSetAccessTokenCookieContent
import pl.starchasers.up.util.getSetRefreshTokenCookieContent
import pl.starchasers.up.util.isCloseTo
import java.time.temporal.ChronoUnit
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenFilter(
    private val tokenService: JwtTokenService
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        val accessTokenString = WebUtils.getCookie(
            (request as HttpServletRequest), JwtTokenService.ACCESS_TOKEN_COOKIE_NAME
        )?.value
        val refreshTokenString = WebUtils.getCookie(
            request, JwtTokenService.REFRESH_TOKEN_COOKIE_NAME
        )?.value

        var newAccessTokenString: String?
        val newRefreshTokenString: String?

        try {
            newRefreshTokenString = refreshRefreshToken(refreshTokenString)
            newAccessTokenString = refreshAccessToken(accessTokenString, newRefreshTokenString)//TODO wtf
            newAccessTokenString = issueAccessToken(newAccessTokenString, newRefreshTokenString)
            val claims = tokenService.parseToken(newAccessTokenString!!)

            //TODO put tokens in authentication and don't use cookies in controllers
//            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
//                claims.subject, null, JwtTokenService.extractGrantedAuthorities(claims)
//            )
            SecurityContextHolder.getContext().authentication = JwtAuthenticationToken(
                claims.subject, null, newRefreshTokenString, JwtTokenService.extractGrantedAuthorities(claims)
            )
            if (newAccessTokenString != accessTokenString) {
                (response as HttpServletResponse).addCookie(
                    getSetAccessTokenCookieContent(newAccessTokenString)
                )
            }
            if (newRefreshTokenString != refreshTokenString && newRefreshTokenString != null) {
                (response as HttpServletResponse).addCookie(
                    getSetRefreshTokenCookieContent(newRefreshTokenString)
                )
            }
        } catch (e: Exception) {
            SecurityContextHolder.clearContext()
        }

        chain.doFilter(request, response)
    }

//    Reissue refresh token if close to expiration
    private fun refreshRefreshToken(refreshToken: String?): String? {
        if (refreshToken == null) return null
        val claims = tokenService.parseToken(refreshToken)
        if (isCloseTo((claims["exp"] as Int).toLong(), 2, ChronoUnit.DAYS)) {
            return tokenService.refreshRefreshToken(refreshToken)
        }
        return refreshToken
    }

    //    Reissue refresh token if close to expiration
    private fun refreshAccessToken(accessToken: String?, refreshToken: String?): String? {
        if (accessToken == null || refreshToken == null) return accessToken
        val claims = tokenService.parseToken(accessToken)
        if (isCloseTo((claims["exp"] as Int).toLong(), 5, ChronoUnit.MINUTES)) {
            return tokenService.issueAccessToken(refreshToken)
        }
        return accessToken
    }

    //    Issue access token if not present, but refresh token is present
    private fun issueAccessToken(accessToken: String?, refreshToken: String?): String? {
        return if (accessToken == null && refreshToken != null) {
            tokenService.issueAccessToken(refreshToken)
        } else {
            accessToken
        }
    }

}
