package pl.starchasers.up

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import pl.starchasers.up.data.model.User
import pl.starchasers.up.service.JwtTokenService

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JpaTestBase {

    @Autowired
    private lateinit var databaseCleaner: DatabaseCleaner

    @Autowired
    private lateinit var jwtTokenService: JwtTokenService

    @AfterEach
    fun cleanup() {
        databaseCleaner.reset()
    }

    private fun getUserAccessToken(user: User): String {
        val refreshToken = jwtTokenService.issueRefreshToken(user)
        return jwtTokenService.issueAccessToken(refreshToken)
    }

    fun MockHttpServletRequestDsl.authorizeAsUser(user: User) {
        header(HttpHeaders.AUTHORIZATION, "Bearer ${getUserAccessToken(user)}")
    }
}
