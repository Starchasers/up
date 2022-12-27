package pl.starchasers.up.controller

import io.kotest.matchers.string.shouldNotBeEmpty
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.post
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.authentication.LoginDTO
import pl.starchasers.up.data.dto.authentication.TokenDTO
import pl.starchasers.up.data.model.User
import pl.starchasers.up.repository.RefreshTokenRepository
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.testdata.UserTestData

internal class AuthenticationControllerTest(
    @Autowired private val jwtTokenService: JwtTokenService,
    @Autowired private val userTestData: UserTestData
) : JpaTestBase() {

    private lateinit var testUser: User

    @BeforeEach
    fun createTestUser() {
        testUser = userTestData.createTestUser()
    }

    @Nested
    inner class LogIn : MockMvcTestBase() {
        private val requestPath = "/api/auth/login"

        @Test
        fun `Given valid data, should return refresh token`() {
            val response: TokenDTO = mockMvc.postJson(requestPath) {
                jsonContent = LoginDTO(UserTestData.DEFAULT_USERNAME, UserTestData.DEFAULT_PASSWORD)
            }.andExpect {
                status { isOk() }
            }.andReturn().parse()

            response.token.shouldNotBeEmpty()
        }

        @Test
        fun `Given incorrect password, should return 403`() {
            mockMvc.postJson(requestPath) {
                jsonContent = LoginDTO(UserTestData.DEFAULT_USERNAME, UserTestData.DEFAULT_PASSWORD + "qwe")
            }.andExpect {
                status { isForbidden() }
            }
        }

        @Test
        fun `Given incorrect username, should return 403`() {
            mockMvc.postJson(requestPath) {
                jsonContent = LoginDTO(UserTestData.DEFAULT_USERNAME + "qwe", UserTestData.DEFAULT_PASSWORD)
            }.andExpect {
                status { isForbidden() }
            }
        }
    }

    @Nested
    inner class LogOut(
        @Autowired private val refreshTokenRepository: RefreshTokenRepository
    ) : MockMvcTestBase() {

        private lateinit var refreshToken: String
        private lateinit var accessToken: String

        private val requestPath = "/api/auth/logout"

        @BeforeEach
        fun createSessions() {
            refreshToken = jwtTokenService.issueRefreshToken(testUser)
            jwtTokenService.issueRefreshToken(testUser)
            jwtTokenService.issueRefreshToken(testUser)
            accessToken = jwtTokenService.issueAccessToken(refreshToken)
        }

        @Test
        fun `Given correct access token, should invalidate all refresh tokens`() {
            mockMvc.post(requestPath) {
                header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            }.andExpect {
                status { isOk() }
            }

            assertTrue(refreshTokenRepository.findAllByUser(testUser).isEmpty())
        }

        @Test
        fun `Given incorrect access token or logged out user, should return 403`() {
            mockMvc.post(requestPath)
                .andExpect {
                    status { isForbidden() }
                }

            assertEquals(3, refreshTokenRepository.findAllByUser(testUser).size)
        }
    }

    @Nested
    inner class GetAccessToken : MockMvcTestBase() {
        private val requestPath = "/api/auth/getAccessToken"

        @Test
        fun `Given valid refresh token, should return access token`() {
            val refreshToken = jwtTokenService.issueRefreshToken(testUser)

            val response: TokenDTO = mockMvc.postJson(requestPath) {
                jsonContent = TokenDTO(refreshToken)
            }.andExpect {
                status { isOk() }
            }.andReturn().parse()

            response.token.shouldNotBeEmpty()
        }

        @Test
        fun `Given invalid refresh token, should return 403`() {
            val refreshToken = jwtTokenService.issueRefreshToken(testUser)
            jwtTokenService.invalidateRefreshToken(refreshToken)

            mockMvc.postJson(requestPath) {
                jsonContent = TokenDTO(refreshToken)
            }.andExpect {
                status { isForbidden() }
            }
        }
    }

    @Nested
    inner class RefreshRefreshToken : MockMvcTestBase() {

        private val requestPath = "/api/auth/refreshToken"

        @Test
        fun `Given valid refresh token, should return new refresh token`() {
            val refreshToken = jwtTokenService.issueRefreshToken(testUser)

            val response: TokenDTO = mockMvc.postJson(requestPath) {
                jsonContent = TokenDTO(refreshToken)
            }.andExpect {
                status { isOk() }
            }.andReturn().parse()

            response.token.shouldNotBeEmpty()
        }

        @Test
        fun `Given invalid refresh token, should return 403`() {
            val refreshToken = jwtTokenService.issueRefreshToken(testUser)
            jwtTokenService.invalidateRefreshToken(refreshToken)

            mockMvc.postJson(requestPath) {
                jsonContent = TokenDTO(refreshToken)
            }.andExpect {
                status { isForbidden() }
            }
        }
    }
}
