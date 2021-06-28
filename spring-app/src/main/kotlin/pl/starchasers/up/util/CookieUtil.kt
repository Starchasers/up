package pl.starchasers.up.util

import pl.starchasers.up.service.JwtTokenService
import javax.servlet.http.HttpServletResponse

fun HttpServletResponse.addCookie(content: String) {
    this.addHeader("Set-Cookie", content)
}

fun getSetEmptyAccessTokenCookieContent(): String = toSetCookieString(
    JwtTokenService.ACCESS_TOKEN_COOKIE_NAME
)

fun getSetEmptyRefreshTokenCookieContent(): String = toSetCookieString(
    JwtTokenService.REFRESH_TOKEN_COOKIE_NAME
)

fun getSetAccessTokenCookieContent(accessToken: String): String = toSetCookieString(
    JwtTokenService.ACCESS_TOKEN_COOKIE_NAME,
    accessToken,
    JwtTokenService.ACCESS_TOKEN_VALID_TIME
)

fun getSetRefreshTokenCookieContent(refreshToken: String): String = toSetCookieString(
    JwtTokenService.REFRESH_TOKEN_COOKIE_NAME,
    refreshToken,
    JwtTokenService.REFRESH_TOKEN_VALID_TIME
)

private fun toSetCookieString(name: String, value: String = "null", maxAge: Long = 0): String =
    SetCookieHeaderValueBuilder(name, value)
        .withMaxAge(maxAge)
        .withPath("/")
        .sameSite()
        .httpOnly()
        .secure() // Comment this to get cookies working in insomnia (or make a https connection to localhost)
        .build()
