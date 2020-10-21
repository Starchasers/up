package pl.starchasers.up.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.starchasers.up.data.dto.authentication.LoginDTO
import pl.starchasers.up.data.dto.authentication.TokenDTO
import pl.starchasers.up.data.value.RawUserPassword
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.security.IsUser
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.UserService
import java.security.Principal

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
        private val userService: UserService,
        private val jwtTokenService: JwtTokenService
) {

    @PostMapping("/login")
    fun login(@RequestBody @Validated loginDTO: LoginDTO): TokenDTO {
        return TokenDTO(jwtTokenService.issueRefreshToken(
                userService.getUserFromCredentials(
                        Username(loginDTO.username),
                        RawUserPassword(loginDTO.password))))
    }

    @IsUser
    @PostMapping("/logout")
    fun logout(principal: Principal) {
        jwtTokenService.invalidateUser(userService.getUser(principal.name.toLong()))
    }

    @PostMapping("/getAccessToken")
    fun getAccessToken(@Validated @RequestBody tokenDTO: TokenDTO): TokenDTO =
            TokenDTO(jwtTokenService.issueAccessToken(tokenDTO.token))

    @PostMapping("/refreshToken")
    fun refreshToken(@Validated @RequestBody tokenDTO: TokenDTO): TokenDTO =
            TokenDTO(jwtTokenService.refreshRefreshToken(tokenDTO.token))
}