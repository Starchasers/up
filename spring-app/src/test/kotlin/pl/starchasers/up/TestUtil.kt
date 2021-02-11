package pl.starchasers.up

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hamcrest.Matcher
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.MethodOrdererContext
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/*fun MockMvcData.isSuccess() {
    printResponseBody()
    statusIsOk()
//    responseJsonPath("$.success").isTrue()
}*/
/*
fun MockMvcData.isError(expectedStatus: HttpStatus) {
    printResponseBody()
    status(expectedStatus)
//    responseJsonPath("$.success").isFalse()
    responseJsonPath("$.message").isNotEmpty()
}*/

/**
 * Response from this test will be included as "Example Response" when generating REST documentation.
 * If none method is annotated, one is chosen at random.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DocumentResponse

class AnnotationMethodOrderer : MethodOrderer {
    override fun orderMethods(context: MethodOrdererContext) {
        context.methodDescriptors.sortBy { method -> if (method.isAnnotated(DocumentResponse::class.java)) 1 else 0 }
    }
}

/**
 * Sorts test execution, so those annotated with DocumentResponse will be executed last
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@TestMethodOrder(AnnotationMethodOrderer::class)
annotation class OrderTests

/*// TODO make library pull request
fun MockMvc.multipart(
    path: Path,
    headers: HttpHeaders? = null,
    fnBuilder: MockMultipartHttpServletRequestBuilder.() -> Unit,
    fn: MockMvcData.() -> Unit
) {

    val builder = MockMvcRequestBuilders
        .multipart(path.url, *path.vars)
        .apply { addHeaders(headers) }

    fnBuilder(builder)

    val resultActions = this.perform(builder)
    val mock = MockMvcData(path, resultActions)
    fn(mock)

//    mock.setupWireMock(headers, method)
//            .addDocumentation(method, docsIdentifier)
}*/

private fun MockHttpServletRequestBuilder.addHeaders(headers: HttpHeaders?) =
    headers?.let { this.headers(it) } ?: this

fun MockHttpServletRequestDsl.content(content: Any) {
    val jsonString = if (content is String) {
        content
    } else {
        jacksonObjectMapper().writeValueAsString(content)
    }
    this.content = jsonString
}

fun HttpHeaders.contentTypeJson() {
    this.contentType = MediaType.APPLICATION_JSON
}

inline fun <reified T> responsePath(expression: String, matcher: Matcher<T>) =
    MockMvcResultMatchers.jsonPath(expression, matcher, T::class)

