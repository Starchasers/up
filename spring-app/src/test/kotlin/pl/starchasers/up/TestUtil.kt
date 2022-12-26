package pl.starchasers.up

import no.skatteetaten.aurora.mockmvc.extensions.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

fun MockMvcData.isSuccess() {
    printResponseBody()
    statusIsOk()
//    responseJsonPath("$.success").isTrue()
}

fun MockMvcData.isError(expectedStatus: HttpStatus) {
    printResponseBody()
    status(expectedStatus)
//    responseJsonPath("$.success").isFalse()
    responseJsonPath("$.message").isNotEmpty()
}

// TODO make library pull request
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
}

private fun MockHttpServletRequestBuilder.addHeaders(headers: HttpHeaders?) =
    headers?.let { this.headers(it) } ?: this
