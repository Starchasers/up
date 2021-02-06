package pl.starchasers.up.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.starchasers.up.data.dto.authentication.LoginDTO
import pl.starchasers.up.data.dto.authentication.TokenDTO
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.UserService
import pl.starchasers.up.util.SetCookieHeaderValueBuilder
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val userService: UserService,
    private val jwtTokenService: JwtTokenService
) {

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
            SetCookieHeaderValueBuilder()
                .withName(JwtTokenService.REFRESH_TOKEN_COOKIE_NAME)
                .withValue(refreshToken)
                .withMaxAge(JwtTokenService.REFRESH_TOKEN_VALID_TIME)
                .withPath("/")
                .httpOnly()
                .build()
        )
        response.addHeader(
            "Set-Cookie",
            SetCookieHeaderValueBuilder()
                .withName(JwtTokenService.ACCESS_TOKEN_COOKIE_NAME)
                .withValue(jwtTokenService.issueAccessToken(refreshToken))
                .withMaxAge(JwtTokenService.ACCESS_TOKEN_VALID_TIME)
                .withPath("/")
                .httpOnly()
                .build()
        )
    }

    @PostMapping("/logout")
    fun logout(@CookieValue(JwtTokenService.REFRESH_TOKEN_COOKIE_NAME) refreshToken: String, response: HttpServletResponse) {
        jwtTokenService.invalidateRefreshToken(refreshToken)
        response.addHeader(
            "Set-Cookie",
            SetCookieHeaderValueBuilder()
                .withName(JwtTokenService.REFRESH_TOKEN_COOKIE_NAME)
                .withValue("null")
                .withMaxAge(JwtTokenService.REFRESH_TOKEN_VALID_TIME)
                .withPath("/")
                .httpOnly()
                .build()
        )
        response.addHeader(
            "Set-Cookie",
            SetCookieHeaderValueBuilder()
                .withName(JwtTokenService.ACCESS_TOKEN_COOKIE_NAME)
                .withValue("null")
                .withMaxAge(JwtTokenService.ACCESS_TOKEN_VALID_TIME)
                .withPath("/")
                .httpOnly()
                .build()
        )
    }

    @PostMapping("/getAccessToken")
    fun getAccessToken(@Validated @CookieValue(JwtTokenService.REFRESH_TOKEN_COOKIE_NAME) refreshToken: String): TokenDTO =
        TokenDTO(jwtTokenService.issueAccessToken(refreshToken))

    @PostMapping("/refreshToken")
    fun refreshToken(@Validated @CookieValue(JwtTokenService.REFRESH_TOKEN_COOKIE_NAME) refreshToken: String): TokenDTO =
        TokenDTO(jwtTokenService.refreshRefreshToken(refreshToken))
}
