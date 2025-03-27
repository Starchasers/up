package pl.starchasers.up.controller

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import pl.starchasers.up.JpaTestBase
import pl.starchasers.up.MockMvcTestBase
import pl.starchasers.up.data.dto.configuration.UserConfigurationDTO
import pl.starchasers.up.data.model.ConfigurationKey
import pl.starchasers.up.parse

internal class ConfigurationControllerTest() : JpaTestBase() {

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
    }
}
