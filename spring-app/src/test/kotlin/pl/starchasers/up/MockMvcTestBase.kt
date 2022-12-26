package pl.starchasers.up

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.UserService

@ExtendWith(RestDocumentationExtension::class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
abstract class MockMvcTestBase {
    @Autowired
    protected lateinit var mapper: ObjectMapper

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jwtTokenService: JwtTokenService

    @Autowired
    private lateinit var userService: UserService

    final fun getAdminAccessToken(): String {
        return jwtTokenService.issueAccessToken(jwtTokenService.issueRefreshToken(userService.getUser(Username("root"))))
    }
}
