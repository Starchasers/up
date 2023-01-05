package pl.starchasers.up.controller

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.get
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.configuration.UserConfigurationDTO
import pl.starchasers.up.data.model.ConfigurationKey
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.service.UserService

internal class ConfigurationControllerTest(
    @Autowired private val userService: UserService
) : JpaTestBase() {

    @Nested
    inner class GetConfiguration : MockMvcTestBase() {

        private val requestPath = "/api/configuration"

        @Test
        fun `Given unauthorized request, should return anonymous configuration`() {
            val response: UserConfigurationDTO = mockMvc.get(requestPath)
                .andExpect {
                    status { isOk() }
                }.andReturn().parse()

            with(response) {
                maxTemporaryFileSize shouldBe ConfigurationKey.ANONYMOUS_MAX_FILE_SIZE.defaultValue.toLong()
                maxFileLifetime shouldBe ConfigurationKey.ANONYMOUS_MAX_FILE_LIFETIME.defaultValue.toLong()
                defaultFileLifetime shouldBe ConfigurationKey.ANONYMOUS_DEFAULT_FILE_LIFETIME.defaultValue.toLong()
                permanentAllowed shouldBe false
                maxPermanentFileSize shouldBe 0
            }
        }

        @Test
        fun `Given authorized request, should return user-specific details`() {
            val user = requireNotNull(userService.findUser(Username("root")))

            val response: UserConfigurationDTO = mockMvc.get(requestPath) {
                authorizeAsUser(user)
            }.andExpect {
                status { isOk() }
            }.andReturn().parse()

            with(response) {
                maxTemporaryFileSize shouldBe ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue.toLong()
                maxFileLifetime shouldBe ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue.toLong()
                defaultFileLifetime shouldBe ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue.toLong()
                permanentAllowed shouldBe true
                maxPermanentFileSize shouldBe ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue.toLong()
            }
        }
    }
}
