package pl.starchasers.up

import capital.scalable.restdocs.AutoDocumentation
import capital.scalable.restdocs.jackson.JacksonResultHandlers
import capital.scalable.restdocs.response.ResponseModifyingPreprocessors.limitJsonArrayLength
import capital.scalable.restdocs.response.ResponseModifyingPreprocessors.replaceBinaryContent
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.cli.CliDocumentation
import org.springframework.restdocs.http.HttpDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.UserService
import javax.servlet.Filter
import javax.servlet.http.Cookie

@ExtendWith(RestDocumentationExtension::class)
@SpringBootTest
@WebAppConfiguration
abstract class MockMvcTestBase {
    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    protected lateinit var mapper: ObjectMapper

    protected lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var springSecurityFilterChain: Filter

    @Autowired
    private lateinit var jwtTokenService: JwtTokenService

    @Autowired
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilters<DefaultMockMvcBuilder>(springSecurityFilterChain)
            .alwaysDo<DefaultMockMvcBuilder>(JacksonResultHandlers.prepareJackson(mapper))
            .alwaysDo<DefaultMockMvcBuilder>(commonDocumentation())
            .apply<DefaultMockMvcBuilder>(
                MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                    .uris()
                    .withScheme("http")
                    .withHost("localhost")
                    .withPort(8080)
                    .and().snippets()
                    .withDefaults(
                        CliDocumentation.curlRequest(),
                        HttpDocumentation.httpRequest(),
                        HttpDocumentation.httpResponse(),
                        AutoDocumentation.requestFields(),
                        AutoDocumentation.responseFields(),
                        AutoDocumentation.pathParameters(),
                        AutoDocumentation.requestParameters(),
                        AutoDocumentation.description(),
                        AutoDocumentation.methodAndPath(),
                        AutoDocumentation.section()
                    )
            )
            .build()
    }

    protected fun commonDocumentation(): RestDocumentationResultHandler {
        return document("{ClassName}", commonResponsePreprocessor())
    }

    protected fun commonResponsePreprocessor(): OperationResponsePreprocessor {
        return preprocessResponse(
            replaceBinaryContent(),
            limitJsonArrayLength(mapper),
            prettyPrint()
        )
    }

    final fun getAdminAccessToken(): String =
        jwtTokenService.issueAccessToken(jwtTokenService.issueRefreshToken(userService.getUser(Username("root"))))

    final fun getAdminAccessTokenCookie(): Cookie =
        Cookie(JwtTokenService.ACCESS_TOKEN_COOKIE_NAME, getAdminAccessToken())

    final fun createRefreshTokenCookie(token: String): Cookie =
        Cookie(JwtTokenService.REFRESH_TOKEN_COOKIE_NAME, token)

    final fun createAccessTokenCookie(token: String): Cookie =
        Cookie(JwtTokenService.ACCESS_TOKEN_COOKIE_NAME, token)
}
