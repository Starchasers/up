package pl.starchasers.up.controller

import no.skatteetaten.aurora.mockmvc.extensions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.test.annotation.DirtiesContext
import pl.starchasers.up.DocumentResponse
import pl.starchasers.up.MockMvcTestBase
import pl.starchasers.up.OrderTests
import pl.starchasers.up.data.model.ConfigurationKey
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.isSuccess
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.UserService
import javax.transaction.Transactional

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
internal class ConfigurationControllerTest(
        @Autowired private val userService: UserService,
        @Autowired private val jwtTokenService: JwtTokenService
) : MockMvcTestBase() {

    @Nested
    @Transactional
    @OrderTests
    inner class GetConfiguration() : MockMvcTestBase() {

        private val requestPath = Path("/api/configuration")

        @Test
        fun `Given unauthorized request, should return anonymous configuration`() {
            mockMvc.get(path = requestPath) {
                isSuccess()
                responseJsonPath("$.maxTemporaryFileSize")
                        .equalsValue(ConfigurationKey.ANONYMOUS_MAX_FILE_SIZE.defaultValue)
                responseJsonPath("$.maxFileLifetime")
                        .equalsValue(ConfigurationKey.ANONYMOUS_MAX_FILE_LIFETIME.defaultValue)
                responseJsonPath("$.defaultFileLifetime")
                        .equalsValue(ConfigurationKey.ANONYMOUS_DEFAULT_FILE_LIFETIME.defaultValue)
                responseJsonPath("$.permanentAllowed")
                        .equalsValue(false)
                responseJsonPath("$.maxPermanentFileSize")
                        .equalsValue(0)
            }
        }

        @Test
        @DocumentResponse
        fun `Given authorized request, should return user-specific details`() {
            val user = requireNotNull(userService.findUser(Username("root")))
            val refreshToken = jwtTokenService.issueRefreshToken(user)
            val accessToken = jwtTokenService.issueAccessToken(refreshToken)

            mockMvc.get(path = requestPath,
            headers = HttpHeaders().authorization(accessToken)) {
                isSuccess()
                responseJsonPath("$.maxTemporaryFileSize")
                        .equalsValue(ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue)
                responseJsonPath("$.maxFileLifetime")
                        .equalsValue(ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue)
                responseJsonPath("$.defaultFileLifetime")
                        .equalsValue(ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue)
                responseJsonPath("$.permanentAllowed")
                        .equalsValue(true)
                responseJsonPath("$.maxPermanentFileSize")
                        .equalsValue(ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue)
            }
        }
    }
}