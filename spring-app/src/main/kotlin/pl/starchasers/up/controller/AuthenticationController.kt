package pl.starchasers.up.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.starchasers.up.data.dto.authentication.LoginDTO
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.exception.AccessDeniedException
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.UserService
import pl.starchasers.up.util.SetCookieHeaderValueBuilder
import java.security.Principal
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val userService: UserService,
    private val jwtTokenService: JwtTokenService
) {

    /**
     * Given correct login and password returns cookies containing refresh and access tokens
     * @param loginDTO DTO containing username and password
     */
    @PostMapping("/login")
    fun login(response: HttpServletResponse, @RequestBody @Validated loginDTO: LoginDTO) {
        val refreshToken = jwtTokenService.issueRefreshToken(
            userService.getUserFromCredentials(
                Username(loginDTO.username),
                RawPassword(loginDTO.password)
            )
        )
        response.addHeader(
            "Set-Cookie",
            toSetCookieString(
                JwtTokenService.REFRESH_TOKEN_COOKIE_NAME,
                refreshToken,
                JwtTokenService.REFRESH_TOKEN_VALID_TIME
            )
        )
        response.addHeader(
            "Set-Cookie",
            toSetCookieString(
                JwtTokenService.ACCESS_TOKEN_COOKIE_NAME,
                jwtTokenService.issueAccessToken(refreshToken),
                JwtTokenService.ACCESS_TOKEN_VALID_TIME
            )
        )
    }

    /**
     * Invalidates refresh token and clears cookies
     */
    @PostMapping("/logout")
    fun logout(
        @CookieValue(JwtTokenService.REFRESH_TOKEN_COOKIE_NAME) refreshToken: String,
        response: HttpServletResponse
    ) {
        jwtTokenService.invalidateRefreshToken(refreshToken)
        clearCookies(response)
    }

    /**
     * Invalidates all refresh tokens for user and clears cookies
     */
    @PostMapping("/logoutAll")
    fun logoutAll(principal: Principal, response: HttpServletResponse) {
        jwtTokenService.invalidateUser(userService.fromPrincipal(principal) ?: throw AccessDeniedException())
        clearCookies(response)
    }

    /**
     * Given valid refresh token returns cookie with new access token
     */
    @PostMapping("/getAccessToken")
    fun getAccessToken(
        @Validated @CookieValue(JwtTokenService.REFRESH_TOKEN_COOKIE_NAME) refreshToken: String,
        response: HttpServletResponse
    ) =
        response.addHeader(
            "Set-Cookie",
            toSetCookieString(
                JwtTokenService.ACCESS_TOKEN_COOKIE_NAME,
                jwtTokenService.issueAccessToken(refreshToken),
                JwtTokenService.ACCESS_TOKEN_VALID_TIME
            )
        )

    /**
     * Returns cookie with new refresh token
     */
    @PostMapping("/refreshToken")
    fun refreshToken(
        @Validated @CookieValue(JwtTokenService.REFRESH_TOKEN_COOKIE_NAME) refreshToken: String,
        response: HttpServletResponse
    ) =
        response.addHeader(
            "Set-Cookie",
            toSetCookieString(
                JwtTokenService.REFRESH_TOKEN_COOKIE_NAME,
                jwtTokenService.refreshRefreshToken(refreshToken),
                JwtTokenService.REFRESH_TOKEN_VALID_TIME
            )
        )

    private fun clearCookies(response: HttpServletResponse) {
        response.addHeader(
            "Set-Cookie",
            toSetCookieString(JwtTokenService.REFRESH_TOKEN_COOKIE_NAME)
        )
        response.addHeader(
            "Set-Cookie",
            toSetCookieString(JwtTokenService.ACCESS_TOKEN_COOKIE_NAME)
        )
    }

    private fun toSetCookieString(name: String, value: String = "null", maxAge: Long = 0): String =
        SetCookieHeaderValueBuilder(name, value)
            .withMaxAge(maxAge)
            .withPath("/")
            .httpOnly()
            .build()
}
