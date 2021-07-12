package pl.starchasers.up.controller

import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.get
import pl.starchasers.up.*
import pl.starchasers.up.data.model.ConfigurationKey
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.UserService

internal class ConfigurationControllerTest(
    @Autowired private val userService: UserService,
    @Autowired private val jwtTokenService: JwtTokenService
) : JpaTestBase() {

    @Nested
    @OrderTests
    inner class GetConfiguration : MockMvcTestBase() {

        private val requestPath = "/api/configuration"

        @Test
        fun `Given unauthorized request, should return anonymous configuration`() {
            mockMvc.get(requestPath).andExpect {
                status { isOk() }
                content {
                    responsePath(
                        "$.maxTemporaryFileSize",
                        equalTo(ConfigurationKey.ANONYMOUS_MAX_FILE_SIZE.defaultValue)
                    )
                    responsePath(
                        "$.maxFileLifetime",
                        equalTo(ConfigurationKey.ANONYMOUS_MAX_FILE_LIFETIME.defaultValue)
                    )
                    responsePath(
                        "$.defaultFileLifetime",
                        equalTo(ConfigurationKey.ANONYMOUS_DEFAULT_FILE_LIFETIME.defaultValue)
                    )
                    responsePath(
                        "$.permanentAllowed",
                        equalTo(false)
                    )
                    responsePath(
                        "$.maxPermanentFileSize",
                        equalTo(0)
                    )
                }
            }
        }

        @Test
        @DocumentResponse
        fun `Given authorized request, should return user-specific details`() {
            val user = requireNotNull(userService.findUser(Username("root")))
            val refreshToken = jwtTokenService.issueRefreshToken(user)
            val accessToken = jwtTokenService.issueAccessToken(refreshToken)

            mockMvc.get(requestPath) {
                cookie(createAccessTokenCookie(accessToken))
            }.andExpect {
                status { isOk() }
                content {
                    responsePath(
                        "$.maxTemporaryFileSize",
                        equalTo(ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue)
                    )
                    responsePath(
                        "$.maxFileLifetime",
                        equalTo(ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue)
                    )
                    responsePath(
                        "$.defaultFileLifetime",
                        equalTo(ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue)
                    )
                    responsePath(
                        "$.permanentAllowed", equalTo(true)
                    )
                    responsePath(
                        "$.maxPermanentFileSize",
                        equalTo(ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue)
                    )
                }
            }
        }
    }
}
