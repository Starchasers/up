package pl.starchasers.up.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.starchasers.up.data.dto.authentication.LoginDTO
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.exception.AccessDeniedException
import pl.starchasers.up.exception.JwtTokenException
import pl.starchasers.up.security.IsUser
import pl.starchasers.up.security.JwtAuthenticationToken
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.UserService
import pl.starchasers.up.util.*
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
        response.addCookie(getSetRefreshTokenCookieContent(refreshToken))
        response.addCookie(getSetAccessTokenCookieContent(jwtTokenService.issueAccessToken(refreshToken)))
    }

    /**
     * Invalidates refresh token and clears cookies
     */
    @IsUser
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
    @IsUser
    @PostMapping("/logoutAll")
    fun logoutAll(principal: Principal, response: HttpServletResponse) {
        jwtTokenService.invalidateUser(userService.fromPrincipal(principal) ?: throw AccessDeniedException())
        clearCookies(response)
    }

    /**
     * Given valid refresh token returns cookie with new access token
     */
    @PostMapping("/getAccessToken")
    @IsUser
    fun getAccessToken(
        response: HttpServletResponse,
        authentication: JwtAuthenticationToken
    ) {
        if (authentication.getRefreshTokenString() != null) {
            val newAccessToken = jwtTokenService.issueAccessToken(authentication.getRefreshTokenString()!!)
            response.addCookie(getSetAccessTokenCookieContent(newAccessToken))
        } else {
            throw JwtTokenException("Can't refresh access token without refresh token")
        }
    }

    /**
     * Returns cookie with new refresh token
     */
    @PostMapping("/refreshToken")
    @IsUser
    fun refreshToken(
        response: HttpServletResponse,
        authentication: JwtAuthenticationToken
    ) {
        if (authentication.getRefreshTokenString() != null) {
            val refreshToken = authentication.getRefreshTokenString()!!
            response.addCookie(getSetRefreshTokenCookieContent(jwtTokenService.refreshRefreshToken(refreshToken)))
        } else {
            throw JwtTokenException("Can't refresh access token without refresh token")
        }
    }

    private fun clearCookies(response: HttpServletResponse) {
        response.addCookie(getSetEmptyRefreshTokenCookieContent())
        response.addCookie(getSetEmptyAccessTokenCookieContent())
    }
}
