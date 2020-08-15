package pl.starchasers.up.controller.admin

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
internal class AuthenticationControllerTest(
        @Autowired private val userService: UserService,
        @Autowired private val jwtTokenService: JwtTokenService
) : MockMvcTestBase() {

    @Transactional
    @OrderTests
    @Nested
    inner class SetConfigurationOption() : MockMvcTestBase() {

        @Test
        @DocumentResponse
        fun `Given valid request, should update configuration`(){
            TODO()
        }

        @Test
        fun `Given incorrect key, should return 400`(){
            TODO()
        }

        @Test
        fun `Given unauthorized request, should return 403`(){
            TODO()
        }
    }

    @Transactional
    @OrderTests
    @Nested
    inner class GetConfigurationOption() : MockMvcTestBase() {

        @Test
        @DocumentResponse
        fun `Given valid request, should return configuration value`(){
            TODO()
        }

        @Test
        fun `Given incorrect key, should return 404`(){
            TODO()
        }

        @Test
        fun `Given unauthorized request, should return 403`(){
            TODO()
        }
    }

    @Transactional
    @OrderTests
    @Nested
    inner class GetGlobalConfiguration() : MockMvcTestBase() {

        @Test
        @DocumentResponse
        fun `Given valid request, should return entire global configuration`(){
            TODO()
        }

        @Test
        fun `Given unauthorized request, should return 403`(){
            TODO()
        }
    }

    @Transactional
    @OrderTests
    @Nested
    inner class SetUserConfiguration() : MockMvcTestBase() {

        @Test
        @DocumentResponse
        fun `Given valid request, should update user`(){
            TODO()
        }

        @Test
        fun `Given incorrect user id, should return 404`(){
            TODO()
        }

        @Test
        fun `Given unauthorized request, should return 403`(){
            TODO()
        }
    }

    @Transactional
    @OrderTests
    @Nested
    inner class GetUserConfiguration() : MockMvcTestBase() {

        @Test
        @DocumentResponse
        fun `Given valid request, should return user configuration`(){
            TODO()
        }

        @Test
        fun `Given incorrect used id, should return 404`(){
            TODO()
        }

        @Test
        fun `Given unauthorized request, should return 403`(){
            TODO()
        }
    }
}