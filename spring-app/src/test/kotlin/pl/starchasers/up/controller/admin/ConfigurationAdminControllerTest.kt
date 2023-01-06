package pl.starchasers.up.controller.admin

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.get
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.configuration.ConfigurationDTO
import pl.starchasers.up.data.model.ConfigurationKey
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.security.Role
import pl.starchasers.up.service.ConfigurationService
import pl.starchasers.up.service.UserService

internal class ConfigurationAdminControllerTest(
    @Autowired private val userService: UserService,
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

    @Nested
    inner class UpdateConfiguration : MockMvcTestBase() {

        private val requestPath = "/api/admin/config"

        private val key1 = ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE
        private val value1 = "123456789"
        private val key2 = ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME
        private val value2 = "987654321"

        @Test
        fun `Given valid request, should update all configuration values`() {
            mockMvc.patchJson(requestPath) {
                jsonContent = ConfigurationDTO(
                    mapOf(Pair(key1, value1), Pair(key2, value2))
                )
                authorizeAsAdmin()
            }.andExpect {
                status { isOk() }
            }

            configurationService.getConfigurationOption(key1) shouldBe value1
            configurationService.getConfigurationOption(key2) shouldBe value2
        }

        @Test
        fun `Given incorrect key, should return 400 and update nothing`() {
            val incorrectKey = "incorrectKey"

            mockMvc.patchJson(requestPath) {
                jsonContent = object {
                    val options = mapOf(Pair(key1.toString(), value1), Pair(incorrectKey, value2))
                }
                authorizeAsAdmin()
            }.andExpect {
                status { isBadRequest() }
            }

            configurationService.getConfigurationOption(key1) shouldBe key1.defaultValue
        }

        @Test
        fun `Given empty map, should update nothing`() {
            mockMvc.patchJson(requestPath) {
                jsonContent = ConfigurationDTO(mapOf())
                authorizeAsAdmin()
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `Given incorrect data type, should return 400 and update nothing`() {
            val incorrectValue = "qwe"
            mockMvc.patchJson(requestPath) {
                jsonContent = ConfigurationDTO(
                    mapOf(Pair(key1, value1), Pair(key2, incorrectValue))
                )
                authorizeAsAdmin()
            }.andExpect {
                status { isBadRequest() }
            }

            configurationService.getConfigurationOption(key1) shouldBe key1.defaultValue
            configurationService.getConfigurationOption(key2) shouldBe key2.defaultValue
        }

        @Test
        fun `Given unauthorized request, should return 403`() {
            mockMvc.patchJson(requestPath) {
                jsonContent = ConfigurationDTO(
                    mapOf(Pair(key1, value1), Pair(key2, value2))
                )
            }.andExpect {
                status { isForbidden() }
            }
        }
    }

    @Nested
    inner class GetAppConfiguration : MockMvcTestBase() {

        private val requestPath = "/api/admin/config"

        @Test
        fun `Given valid request, should return entire global configuration`() {
            val response: ConfigurationDTO = mockMvc.get(requestPath) {
                authorizeAsAdmin()
            }.andExpect {
                status { isOk() }
            }.andReturn().parse()

            with(response) {
                options[ConfigurationKey.ANONYMOUS_MAX_FILE_SIZE] shouldBe ConfigurationKey.ANONYMOUS_MAX_FILE_SIZE.defaultValue
                options[ConfigurationKey.ANONYMOUS_DEFAULT_FILE_LIFETIME] shouldBe ConfigurationKey.ANONYMOUS_DEFAULT_FILE_LIFETIME.defaultValue
                options[ConfigurationKey.ANONYMOUS_MAX_FILE_LIFETIME] shouldBe ConfigurationKey.ANONYMOUS_MAX_FILE_LIFETIME.defaultValue
                options[ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE] shouldBe ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue
                options[ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE] shouldBe ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue
                options[ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME] shouldBe ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue
                options[ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME] shouldBe ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue
            }
        }

        @Test
        fun `Given unauthorized request, should return 403`() {
            mockMvc.get(requestPath) {
                authorizeAsUser(testUser)
            }.andExpect {
                status { isForbidden() }
            }
        }
    }
}
