package pl.starchasers.up.controller

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import pl.starchasers.up.DocumentResponse
import pl.starchasers.up.MockMvcTestBase
import pl.starchasers.up.OrderTests
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

        @Test
        fun `Given unauthorized request, should return anonymous configuration`() {
            TODO()
        }

        @Test
        @DocumentResponse
        fun `Given authorized request, should return user-specific details`() {
            TODO()
        }
    }
}