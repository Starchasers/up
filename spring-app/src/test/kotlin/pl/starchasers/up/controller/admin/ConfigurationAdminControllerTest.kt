package pl.starchasers.up.controller.admin

import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.configuration.ConfigurationDTO
import pl.starchasers.up.data.model.ConfigurationKey
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.security.Role
import pl.starchasers.up.service.ConfigurationService
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.UserService
import javax.servlet.http.Cookie

internal class ConfigurationAdminControllerTest(
    @Autowired private val userService: UserService,
    @Autowired private val jwtTokenService: JwtTokenService,
    @Autowired private val configurationService: ConfigurationService
) : JpaTestBase() {

    private lateinit var testUser: User

    @BeforeEach
    fun createTestUser() {
        testUser = userService.createUser(
            Username("unauthorizedUser"),
            RawPassword("password"),
            null,
            Role.USER
        )
    }

    private fun getUserAccessToken(): String {
        val refreshToken = jwtTokenService.issueRefreshToken(testUser)
        return jwtTokenService.issueAccessToken(refreshToken)
    }

    private fun getUserAccessTokenCookie(): Cookie =
        Cookie(JwtTokenService.ACCESS_TOKEN_COOKIE_NAME, getUserAccessToken())

    @OrderTests
    @Nested
    inner class UpdateConfiguration : MockMvcTestBase() {

        private val requestPath = "/api/admin/config"

        private val key1 = ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE
        private val value1 = "123456789"
        private val key2 = ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME
        private val value2 = "987654321"

        @Test
        @DocumentResponse
        fun `Given valid request, should update all configuration values`() {
            mockMvc.patch(requestPath) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
                content(ConfigurationDTO(mapOf(Pair(key1, value1), Pair(key2, value2))))
            }.andExpect {
                status { isOk() }
            }

            assertEquals(value1, configurationService.getConfigurationOption(key1))
            assertEquals(value2, configurationService.getConfigurationOption(key2))
        }

        @Test
        fun `Given incorrect key, should return 400 and update nothing`() {
            val incorrectKey = "incorrectKey"
            mockMvc.patch(requestPath) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
                content(object {
                    val options = mapOf(Pair(key1.toString(), value1), Pair(incorrectKey, value2))
                })
            }.andExpect {
                status { isBadRequest() }
            }

            assertEquals(configurationService.getConfigurationOption(key1), key1.defaultValue)
        }

        @Test
        fun `Given empty map, should update nothing`() {
            mockMvc.patch(requestPath) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
                content(ConfigurationDTO(mapOf()))
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `Given incorrect data type, should return 400 and update nothing`() {
            val incorrectValue = "qwe"
            mockMvc.patch(requestPath) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
                content(ConfigurationDTO(mapOf(Pair(key1, value1), Pair(key2, incorrectValue))))
            }.andExpect {
                status { isBadRequest() }
            }

            assertEquals(configurationService.getConfigurationOption(key1), key1.defaultValue)
            assertEquals(configurationService.getConfigurationOption(key2), key2.defaultValue)
        }

        @Test
        fun `Given unauthorized request, should return 403`() {
            mockMvc.patch(requestPath) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
                content(ConfigurationDTO(mapOf(Pair(key1, value1), Pair(key2, value2))))
            }
        }
    }

    @OrderTests
    @Nested
    inner class GetAppConfiguration : MockMvcTestBase() {

        private val requestPath = "/api/admin/config"

        @Test
        @DocumentResponse
        fun `Given valid request, should return entire global configuration`() {
            mockMvc.get(requestPath) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
            }.andExpect {
                status { isOk() }
                content {
                    responsePath(
                        "$.options.ANONYMOUS_MAX_FILE_SIZE",
                        Matchers.equalTo(ConfigurationKey.ANONYMOUS_MAX_FILE_SIZE.defaultValue)
                    )
                    responsePath(
                        "$.options.ANONYMOUS_DEFAULT_FILE_LIFETIME",
                        Matchers.equalTo(ConfigurationKey.ANONYMOUS_DEFAULT_FILE_LIFETIME.defaultValue)
                    )
                    responsePath(
                        "$.options.ANONYMOUS_MAX_FILE_LIFETIME",
                        Matchers.equalTo(ConfigurationKey.ANONYMOUS_MAX_FILE_LIFETIME.defaultValue)
                    )
                    responsePath(
                        "$.options.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE",
                        Matchers.equalTo(ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue)
                    )
                    responsePath(
                        "$.options.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE",
                        Matchers.equalTo(ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue)
                    )
                    responsePath(
                        "$.options.DEFAULT_USER_DEFAULT_FILE_LIFETIME",
                        Matchers.equalTo(ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue)
                    )
                    responsePath(
                        "$.options.DEFAULT_USER_MAX_FILE_LIFETIME",
                        Matchers.equalTo(ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue)
                    )
                }
            }
        }

        @Test
        fun `Given unauthorized request, should return 403`() {
            mockMvc.get(requestPath) {
                headers { contentTypeJson() }
                cookie(getUserAccessTokenCookie())
            }.andExpect {
                status { isForbidden() }
            }
        }
    }
}
