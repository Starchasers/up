package pl.starchasers.up.util

import org.springframework.http.ResponseCookie
import pl.starchasers.up.service.JwtTokenService
import java.time.Duration
import javax.servlet.http.HttpServletResponse

fun HttpServletResponse.addCookie(cookie: ResponseCookie) = addCookie(cookie.toString())

fun HttpServletResponse.addCookie(content: String) {
    this.addHeader("Set-Cookie", content)
}

fun getSetEmptyAccessTokenCookie(): ResponseCookie = getCookie(
    JwtTokenService.ACCESS_TOKEN_COOKIE_NAME
)

fun getSetEmptyRefreshTokenCookie(): ResponseCookie = getCookie(
    JwtTokenService.REFRESH_TOKEN_COOKIE_NAME
)

fun getSetAccessTokenCookie(accessToken: String): ResponseCookie = getCookie(
    JwtTokenService.ACCESS_TOKEN_COOKIE_NAME,
    accessToken,
    JwtTokenService.ACCESS_TOKEN_VALID_TIME
)

fun getSetRefreshTokenCookie(refreshToken: String): ResponseCookie = getCookie(
    JwtTokenService.REFRESH_TOKEN_COOKIE_NAME,
    refreshToken,
    JwtTokenService.REFRESH_TOKEN_VALID_TIME
)

private fun getCookie(name: String, value: String = "null", maxAge: Duration = Duration.ZERO): ResponseCookie =
    ResponseCookie.from(name, value)
        .maxAge(maxAge)
        .path("/")
        .sameSite("Strict")
        .httpOnly(true)
        //TODO make configurable/disable in dev profile
        .secure(true)// Comment this to get cookies working in insomnia (or make a https connection to localhost)
        .build()
