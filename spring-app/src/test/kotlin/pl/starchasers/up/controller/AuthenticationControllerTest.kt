package pl.starchasers.up.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.post
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.authentication.LoginDTO
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.RefreshTokenId
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
        private val loginRequestPath = "/api/auth/login"

        @Test
        @DocumentResponse
        fun `Given valid data, should return refresh token`() {
            mockMvc.post(loginRequestPath) {
                headers { contentTypeJson() }
                content(LoginDTO(testUserUsername.value, testUserPassword.value))
            }.andExpect {
                status { isOk() }
                cookie { exists(JwtTokenService.REFRESH_TOKEN_COOKIE_NAME) }
            }
        }

        @Test
        fun `Given incorrect password, should return 403`() {
            mockMvc.post(loginRequestPath) {
                headers { contentTypeJson() }
                content(LoginDTO(testUserUsername.value, testUserPassword.value + "qwe"))
            }.andExpect {
                status { isForbidden() }
            }
        }

        @Test
        fun `Given incorrect username, should return 403`() {
            mockMvc.post(loginRequestPath) {
                headers { contentTypeJson() }
                content(LoginDTO(testUserUsername.value + "qwe", testUserPassword.value))
            }.andExpect {
                status { isForbidden() }
            }
        }

        @Test
        fun `Given missing fields, should return 400`() {
            mockMvc.post(loginRequestPath) {
                headers { contentTypeJson() }
                content(object {})
            }.andExpect {
                status { isBadRequest() }
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

        private val logoutRequestPath = "/api/auth/logout"

        @BeforeEach
        fun createSessions() {
            refreshToken = jwtTokenService.issueRefreshToken(testUser)
            jwtTokenService.issueRefreshToken(testUser)
            jwtTokenService.issueRefreshToken(testUser)
            accessToken = jwtTokenService.issueAccessToken(refreshToken)
        }

        @Test
        @DocumentResponse
        fun `Given correct refresh token, should invalidate refresh token`() {
            mockMvc.post(logoutRequestPath) {
                cookie(createRefreshTokenCookie(refreshToken))
            }.andExpect {
                status { isOk() }
            }
            assertEquals(refreshTokenRepository.findFirstByTokenAndUser(RefreshTokenId(refreshToken), testUser), null)
        }

        @Test
        fun `Given incorrect access token or logged out user, should return 403`() {
            mockMvc.post(logoutRequestPath).andExpect {
                status { isBadRequest() }
            }

            assertEquals(3, refreshTokenRepository.findAllByUser(testUser).size)
        }
    }

    @OrderTests
    @Nested
    inner class LogOutAll(
        @Autowired private val refreshTokenRepository: RefreshTokenRepository
    ) : MockMvcTestBase() {

        private lateinit var refreshToken: String
        private lateinit var accessToken: String

        private val logoutAllRequestPath = "/api/auth/logoutAll"

        @BeforeEach
        fun createSessions() {
            refreshToken = jwtTokenService.issueRefreshToken(testUser)
            jwtTokenService.issueRefreshToken(testUser)
            jwtTokenService.issueRefreshToken(testUser)
            accessToken = jwtTokenService.issueAccessToken(refreshToken)
        }

        @Test
        @DocumentResponse
        fun `Given correct refresh token, should invalidate refresh token`() {
            mockMvc.post(logoutAllRequestPath) {
                cookie(createRefreshTokenCookie(refreshToken))
            }.andExpect {
                status { isOk() }
            }
            assertTrue(refreshTokenRepository.findAllByUser(testUser).isEmpty())
        }
    }

    @OrderTests
    @Nested
    inner class GetAccessToken : MockMvcTestBase() {
        private val accessTokenRequestPath = "/api/auth/getAccessToken"

        @Test
        @DocumentResponse
        fun `Given valid refresh token, should return access token`() {
            val refreshToken = jwtTokenService.issueRefreshToken(testUser)

            mockMvc.post(accessTokenRequestPath) {
                headers { contentTypeJson() }
                cookie(createRefreshTokenCookie(refreshToken))
            }.andExpect {
                status { isOk() }
                cookie { exists(JwtTokenService.ACCESS_TOKEN_COOKIE_NAME) }
            }
        }

        @Test
        fun `Given invalid refresh token, should return 403`() {
            val refreshToken = jwtTokenService.issueRefreshToken(testUser)

            jwtTokenService.invalidateRefreshToken(
                refreshToken
            )
            mockMvc.post(accessTokenRequestPath) {
                headers { contentTypeJson() }
                cookie(createRefreshTokenCookie(refreshToken))
            }.andExpect {
                status { isForbidden() }
            }
        }
    }

    @OrderTests
    @Nested
    inner class RefreshRefreshToken : MockMvcTestBase() {

        private val refreshTokenRequestPath = "/api/auth/refreshToken"

        @Test
        @DocumentResponse
        fun `Given valid refresh token, should return new refresh token`() {
            val refreshToken = jwtTokenService.issueRefreshToken(testUser)

            mockMvc.post(refreshTokenRequestPath) {
                headers { contentTypeJson() }
                cookie(createRefreshTokenCookie(refreshToken))
            }.andExpect {
                status { isOk() }
                cookie { exists(JwtTokenService.REFRESH_TOKEN_COOKIE_NAME) }
            }
        }

        @Test
        fun `Given invalid refresh token, should return 403`() {
            val refreshToken = jwtTokenService.issueRefreshToken(testUser)
            jwtTokenService.invalidateRefreshToken(refreshToken)

            mockMvc.post(refreshTokenRequestPath) {
                headers { contentTypeJson() }
                cookie(createRefreshTokenCookie(refreshToken))
            }.andExpect {
                status { isForbidden() }
            }
        }
    }
}
