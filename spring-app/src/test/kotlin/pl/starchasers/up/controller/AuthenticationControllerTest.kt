package pl.starchasers.up.controller

import no.skatteetaten.aurora.mockmvc.extensions.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.authentication.LoginDTO
import pl.starchasers.up.data.dto.authentication.TokenDTO
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.repository.RefreshTokenRepository
import pl.starchasers.up.security.Role
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.UserService


internal class AuthenticationControllerTest(
    @Autowired private val userService: UserService,
    @Autowired private val jwtTokenService: JwtTokenService
) : JpaTestBase() {

    private val testUserUsername: Username = Username("testUser")
    private val testUserPassword: RawPassword = RawPassword("examplePassword")
    private lateinit var testUser: User

    @BeforeEach
    fun createTestUser() {
        testUser = userService.createUser(testUserUsername, testUserPassword, null, Role.USER)
    }

    @OrderTests
    @Nested
    inner class LogIn : MockMvcTestBase() {
        private val loginRequestPath = Path("/api/auth/login")

        @Test
        @DocumentResponse
        fun `Given valid data, should return refresh token`() {
            mockMvc.post(
                path = loginRequestPath,
                headers = HttpHeaders().contentTypeJson(),
                body = LoginDTO(testUserUsername.value, testUserPassword.value)
            ) {
                isSuccess()
                responseJsonPath("$.token").isNotEmpty()
            }
        }

        @Test
        fun `Given incorrect password, should return 403`() {
            mockMvc.post(
                path = loginRequestPath,
                headers = HttpHeaders().contentTypeJson(),
                body = LoginDTO(testUserUsername.value, testUserPassword.value + "qwe")
            ) {
                isError(HttpStatus.FORBIDDEN)
            }
        }

        @Test
        fun `Given incorrect username, should return 403`() {
            mockMvc.post(
                path = loginRequestPath,
                headers = HttpHeaders().contentTypeJson(),
                body = LoginDTO(testUserUsername.value + "qwe", testUserPassword.value)
            ) {
                isError(HttpStatus.FORBIDDEN)
            }
        }

        @Test
        fun `Given missing fields, should return 400`() {
            mockMvc.post(
                path = loginRequestPath,
                headers = HttpHeaders().contentTypeJson(),
                body = object {}
            ) {
                isError(HttpStatus.BAD_REQUEST)
            }
        }

    }

    @OrderTests
    @Nested
    inner class LogOut(
        @Autowired private val refreshTokenRepository: RefreshTokenRepository
    ) : MockMvcTestBase() {

        private lateinit var refreshToken: String
        private lateinit var accessToken: String

        private val logoutRequestPath = Path("/api/auth/logout")

        @BeforeEach
        fun createSessions() {
            refreshToken = jwtTokenService.issueRefreshToken(testUser)
            jwtTokenService.issueRefreshToken(testUser)
            jwtTokenService.issueRefreshToken(testUser)
            accessToken = jwtTokenService.issueAccessToken(refreshToken)
        }

        @Test
        @DocumentResponse
        fun `Given correct access token, should invalidate all refresh tokens`() {
            mockMvc.post(
                path = logoutRequestPath,
                headers = HttpHeaders().authorization(accessToken)
            ) {
                isSuccess()
            }
            assertTrue(refreshTokenRepository.findAllByUser(testUser).isEmpty())
        }

        @Test
        fun `Given incorrect access token or logged out user, should return 403`() {
            mockMvc.post(
                path = logoutRequestPath
            ) {
                isError(HttpStatus.FORBIDDEN)
            }

            assertEquals(3, refreshTokenRepository.findAllByUser(testUser).size)
        }

    }

    @OrderTests
    @Nested
    inner class GetAccessToken() : MockMvcTestBase() {
        private val getAccessTokenRequestPath = Path("/api/auth/getAccessToken")


        @Test
        @DocumentResponse
        fun `Given valid refresh token, should return access token`() {
            val refreshToken = jwtTokenService.issueRefreshToken(testUser)

            mockMvc.post(
                path = getAccessTokenRequestPath,
                headers = HttpHeaders().contentTypeJson(),
                body = TokenDTO(refreshToken)
            ) {
                isSuccess()
                responseJsonPath("$.token").isNotEmpty()
            }
        }

        @Test
        fun `Given invalid refresh token, should return 403`() {
            val refreshToken = jwtTokenService.issueRefreshToken(testUser)

            jwtTokenService.invalidateRefreshToken(
                refreshToken
            )
            mockMvc.post(
                path = getAccessTokenRequestPath,
                headers = HttpHeaders().contentTypeJson(),
                body = mapper.writeValueAsString(TokenDTO(refreshToken))
            ) {
                isError(HttpStatus.FORBIDDEN)
            }
        }

    }

    @OrderTests
    @Nested
    inner class RefreshRefreshToken() : MockMvcTestBase() {

        private val refreshTokenRequestPath = Path("/api/auth/refreshToken")


        @Test
        @DocumentResponse
        fun `Given valid refresh token, should return new refresh token`() {
            val refreshToken = jwtTokenService.issueRefreshToken(testUser)

            mockMvc.post(
                path = refreshTokenRequestPath,
                headers = HttpHeaders().contentTypeJson(),
                body = TokenDTO(refreshToken)
            ) {
                isSuccess()
                responseJsonPath("$.token").isNotEmpty()
            }
        }

        @Test
        fun `Given invalid refresh token, should return 403`() {
            val refreshToken = jwtTokenService.issueRefreshToken(testUser)
            jwtTokenService.invalidateRefreshToken(refreshToken)

            mockMvc.post(
                path = refreshTokenRequestPath,
                headers = HttpHeaders().contentTypeJson(),
                body = TokenDTO(refreshToken)
            ) {
                isError(HttpStatus.FORBIDDEN)
            }

        }
    }
}