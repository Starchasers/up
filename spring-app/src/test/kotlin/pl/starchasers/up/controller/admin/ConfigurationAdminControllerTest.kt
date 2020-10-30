package pl.starchasers.up.controller.admin

import no.skatteetaten.aurora.mockmvc.extensions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.configuration.ConfigurationDTO
import pl.starchasers.up.data.dto.configuration.ConfigurationOptionDTO
import pl.starchasers.up.data.dto.configuration.UpdateUserConfigurationDTO
import pl.starchasers.up.data.model.ConfigurationKey
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.security.Role
import pl.starchasers.up.service.ConfigurationService
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.UserService
import javax.transaction.Transactional


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
internal class ConfigurationAdminControllerTest(
        @Autowired private val userService: UserService,
        @Autowired private val jwtTokenService: JwtTokenService,
        @Autowired private val configurationService: ConfigurationService
) : MockMvcTestBase() {

    private val testUser = userService.createUser(
            Username("unauthorizedUser"),
            RawPassword("password"),
            null,
            Role.USER)

    private fun getUserAccessToken(): String {
        val refreshToken = jwtTokenService.issueRefreshToken(testUser)
        return jwtTokenService.issueAccessToken(refreshToken)
    }

    @Transactional
    @OrderTests
    @Nested
    inner class UpdateConfiguration : MockMvcTestBase() {

        private val requestPath = Path("/api/admin/config")

        private val key1 = ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE
        private val value1 = "123456789"
        private val key2 = ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME
        private val value2 = "987654321"

        @Test
        @DocumentResponse
        fun `Given valid request, should update all configuration values`() {
            mockMvc.put(
                    path = requestPath,
                    headers = HttpHeaders().contentTypeJson().authorization(getAdminAccessToken()),
                    body = ConfigurationDTO(
                            mapOf(Pair(key1, value1), Pair(key2, value2))
                    )
            ) {
                statusIsOk()
            }

            assertEquals(value1, configurationService.getConfigurationOption(key1))
            assertEquals(value2, configurationService.getConfigurationOption(key2))
        }

        @Test
        fun `Given incorrect key, should return 400 and update nothing`() {
            val incorrectKey = "incorrectKey"

            mockMvc.put(
                    path = requestPath,
                    headers = HttpHeaders().contentTypeJson().authorization(getAdminAccessToken()),
                    body = object {
                        val options = mapOf(Pair(key1.toString(), value1), Pair(incorrectKey, value2))
                    }
            ) {
                isError(HttpStatus.BAD_REQUEST)
            }

            assertEquals(configurationService.getConfigurationOption(key1), key1.defaultValue)
        }

        @Test
        fun `Given empty map, should update nothing`() {
            mockMvc.put(
                    path = requestPath,
                    headers = HttpHeaders().contentTypeJson().authorization(getAdminAccessToken()),
                    body = ConfigurationDTO(mapOf())
            ) {
                isSuccess()
            }
        }

        @Test
        fun `Given incorrect data type, should return 400 and update nothing`() {
            val incorrectValue = "qwe"

            mockMvc.put(
                    path = requestPath,
                    headers = HttpHeaders().contentTypeJson().authorization(getAdminAccessToken()),
                    body = ConfigurationDTO(
                            mapOf(Pair(key1, value1), Pair(key2, incorrectValue))
                    )
            ) {
                isError(HttpStatus.BAD_REQUEST)
            }

            assertEquals(configurationService.getConfigurationOption(key1), key1.defaultValue)
            assertEquals(configurationService.getConfigurationOption(key2), key2.defaultValue)
        }

        @Test
        fun `Given unauthorized request, should return 403`() {
            mockMvc.put(
                    path = requestPath,
                    headers = HttpHeaders().contentTypeJson().authorization(getUserAccessToken()),
                    body = ConfigurationDTO(
                            mapOf(Pair(key1, value1), Pair(key2, value2))
                    )
            ) {
                isError(HttpStatus.FORBIDDEN)
            }
        }
    }

    @Transactional
    @OrderTests
    @Nested
    inner class GetAppConfiguration : MockMvcTestBase() {

        private val requestPath = Path("/api/admin/config")

        @Test
        @DocumentResponse
        fun `Given valid request, should return entire global configuration`() {
            mockMvc.get(
                    path = requestPath,
                    headers = HttpHeaders().authorization(getAdminAccessToken())
            ) {
                isSuccess()
                responseJsonPath("$.options.ANONYMOUS_MAX_FILE_SIZE")
                        .equalsValue(ConfigurationKey.ANONYMOUS_MAX_FILE_SIZE.defaultValue)
                responseJsonPath("$.options.ANONYMOUS_DEFAULT_FILE_LIFETIME")
                        .equalsValue(ConfigurationKey.ANONYMOUS_DEFAULT_FILE_LIFETIME.defaultValue)
                responseJsonPath("$.options.ANONYMOUS_MAX_FILE_LIFETIME")
                        .equalsValue(ConfigurationKey.ANONYMOUS_MAX_FILE_LIFETIME.defaultValue)
                responseJsonPath("$.options.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE")
                        .equalsValue(ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue)
                responseJsonPath("$.options.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE")
                        .equalsValue(ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue)
                responseJsonPath("$.options.DEFAULT_USER_DEFAULT_FILE_LIFETIME")
                        .equalsValue(ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue)
                responseJsonPath("$.options.DEFAULT_USER_MAX_FILE_LIFETIME")
                        .equalsValue(ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue)
            }
        }

        @Test
        fun `Given unauthorized request, should return 403`() {
            mockMvc.get(
                    path = requestPath,
                    headers = HttpHeaders().authorization(getUserAccessToken())
            ) {
                isError(HttpStatus.FORBIDDEN)
            }
        }
    }
}