package pl.starchasers.up.controller.admin

import no.skatteetaten.aurora.mockmvc.extensions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.configuration.UserConfigurationDTO
import pl.starchasers.up.data.dto.configuration.ConfigurationDTO
import pl.starchasers.up.data.dto.configuration.ConfigurationOptionDTO
import pl.starchasers.up.data.dto.configuration.UpdateUserConfigurationDTO
import pl.starchasers.up.data.model.ConfigurationKey
import pl.starchasers.up.data.model.User
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

    private fun getAdminAccessToken(): String {
        val admin = requireNotNull(userService.findUser("root"))
        val refreshToken = jwtTokenService.issueRefreshToken(admin)
        return jwtTokenService.issueAccessToken(refreshToken)
    }

    private val testUser = userService.createUser("unauthorizedUser", "password", null, Role.USER)

    private fun getUserAccessToken(): String {
        val refreshToken = jwtTokenService.issueRefreshToken(testUser)
        return jwtTokenService.issueAccessToken(refreshToken)
    }

    @Transactional
    @OrderTests
    @Nested
    inner class SetConfigurationOption : MockMvcTestBase() {

        private val requestPath = Path("/api/admin/config")
        private val accessToken = getAdminAccessToken()

        @Test
        @DocumentResponse
        fun `Given valid request, should update configuration`() {
            val newValue = "123456789"
            val key = ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE
            mockMvc.put(path = requestPath,
                    headers = HttpHeaders().contentTypeJson().authorization(accessToken),
                    body = ConfigurationOptionDTO(
                            key,
                            newValue
                    )) {
                isSuccess()
            }
            assertEquals(newValue, configurationService.getConfigurationOption(key))
        }

        @Test
        fun `Given incorrect key, should return 400`() {
            mockMvc.put(path = requestPath,
                    headers = HttpHeaders().contentTypeJson().authorization(accessToken),
                    body = object {
                        val key = "incorrectKey"
                        val value = "123456789"
                    }) {
                isError(HttpStatus.BAD_REQUEST)
            }
        }

        @Test
        fun `Given incorrect value type, should return 400`() {
            val newValue = "qwe"
            val key = ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE
            mockMvc.put(path = requestPath,
                    headers = HttpHeaders().contentTypeJson().authorization(accessToken),
                    body = ConfigurationOptionDTO(
                            key,
                            newValue
                    )) {
                isError(HttpStatus.BAD_REQUEST)
            }

            assertEquals(configurationService.getConfigurationOption(key), key.defaultValue)
        }

        @Test
        fun `Given unauthorized request, should return 403`() {
            val newValue = "123456789"
            val key = ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE
            mockMvc.put(path = requestPath,
                    headers = HttpHeaders().contentTypeJson().authorization(getUserAccessToken()),
                    body = ConfigurationOptionDTO(
                            key,
                            newValue
                    )) {
                isError(HttpStatus.FORBIDDEN)
            }
        }
    }

    @Transactional
    @OrderTests
    @Nested
    inner class SetConfiguration() : MockMvcTestBase() {

        private val requestPath = Path("/api/admin/config/all")
        private val accessToken = getAdminAccessToken()

        private val key1 = ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE
        private val value1 = "123456789"
        private val key2 = ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME
        private val value2 = "987654321"

        @Test
        @DocumentResponse
        fun `Given valid request, should update all configuration values`() {
            mockMvc.put(
                    path = requestPath,
                    headers = HttpHeaders().contentTypeJson().authorization(accessToken),
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
                    headers = HttpHeaders().contentTypeJson().authorization(accessToken),
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
                    headers = HttpHeaders().contentTypeJson().authorization(accessToken),
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
                    headers = HttpHeaders().contentTypeJson().authorization(accessToken),
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
    inner class GetGlobalConfiguration() : MockMvcTestBase() {

        private val requestPath = Path("/api/admin/config/all")
        private val accessToken = getAdminAccessToken()

        @Test
        @DocumentResponse
        fun `Given valid request, should return entire global configuration`() {
            mockMvc.get(
                    path = requestPath,
                    headers = HttpHeaders().authorization(accessToken)
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

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Transactional
    @OrderTests
    @Nested
    inner class SetUserConfiguration() : MockMvcTestBase() {

        private fun requestPath(userId: Long) = Path("/api/admin/config/user/$userId")
        private val accessToken = getAdminAccessToken()

        private val testUser2: User =userService.createUser("setConfigurationTestUser", "password", null, Role.USER)

        @Test
        @Transactional
        @DocumentResponse
        fun `Given valid request, should update user`() {
            mockMvc.put(
                    path = requestPath(testUser2.id),
                    headers = HttpHeaders().contentTypeJson().authorization(accessToken),
                    body = UpdateUserConfigurationDTO(
                            1,
                            123,
                            12,
                            1234
                    )
            ) {
                isSuccess()
            }
            flush()
            val updatedUser = userService.getUser(testUser2.id)
            assertEquals(1, updatedUser.maxTemporaryFileSize.value)
            assertEquals(12, updatedUser.defaultFileLifetime.value)
            assertEquals(123, updatedUser.maxFileLifetime.value)
            assertEquals(1234, updatedUser.maxPermanentFileSize.value)
        }

        @Test
        fun `Given incorrect user id, should return 404`() {
            mockMvc.put(
                    path = requestPath(12345),
                    headers = HttpHeaders().contentTypeJson().authorization(accessToken),
                    body = UpdateUserConfigurationDTO(
                            1,
                            123,
                            12,
                            1234
                    )
            ) {
                isError(HttpStatus.NOT_FOUND)
            }
        }

        @Test
        fun `Given incorrect data type, should return 400`() {
            mockMvc.put(
                    path = requestPath(testUser2.id),
                    headers = HttpHeaders().contentTypeJson().authorization(accessToken),
                    body = object {
                        val maxTemporaryFileSize = 1
                        val defaultFileLifetime = 12
                        val maxFileLifetime = 123
                        val maxPermanentFileSize = "qwe"
                    }
            ) {
                isError(HttpStatus.BAD_REQUEST)
            }
            assertEquals(ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue.toLong(),
                    testUser2.maxTemporaryFileSize.value)
            assertEquals(ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue.toLong(),
                    testUser2.maxFileLifetime.value)
            assertEquals(ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue.toLong(),
                    testUser2.defaultFileLifetime.value)
            assertEquals(ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue.toLong(),
                    testUser2.maxPermanentFileSize.value)
        }

        @Test
        fun `Given unauthorized request, should return 403`() {
            mockMvc.put(
                    path = requestPath(testUser2.id),
                    headers = HttpHeaders().contentTypeJson().authorization(getUserAccessToken()),
                    body = UpdateUserConfigurationDTO(
                            1,
                            123,
                            12,
                            1234
                    )
            ) {
                isError(HttpStatus.FORBIDDEN)
            }
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Transactional
    @OrderTests
    @Nested
    inner class GetUserConfiguration() : MockMvcTestBase() {

        private val testUser2 = userService.createUser("testUser2", "password", null, Role.USER)
        private fun requestPath(userId: Long) = Path("/api/admin/config/user/$userId")
        private val accessToken = getAdminAccessToken()

        @Test
        @DocumentResponse
        fun `Given valid request, should return user configuration`() {
            mockMvc.get(
                    path = requestPath(testUser2.id),
                    headers = HttpHeaders().contentTypeJson().authorization(accessToken)
            ) {
                isSuccess()
                responseJsonPath("$.maxTemporaryFileSize")
                        .equalsValue(ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue)
                responseJsonPath("$.maxFileLifetime")
                        .equalsValue(ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue)
                responseJsonPath("$.defaultFileLifetime")
                        .equalsValue(ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue)
                responseJsonPath("$.permanentAllowed").equalsValue(true)
                responseJsonPath("$.maxPermanentFileSize")
                        .equalsValue(ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue)
            }
        }

        @Test
        fun `Given incorrect used id, should return 404`() {
            mockMvc.get(
                    path = requestPath(12345),
                    headers = HttpHeaders().contentTypeJson().authorization(accessToken)
            ) {
                isError(HttpStatus.NOT_FOUND)
            }
        }

        @Test
        fun `Given unauthorized request, should return 403`() {
            mockMvc.get(
                    path = requestPath(testUser2.id),
                    headers = HttpHeaders().contentTypeJson().authorization(getUserAccessToken())
            ) {
                isError(HttpStatus.FORBIDDEN)
            }
        }
    }
}