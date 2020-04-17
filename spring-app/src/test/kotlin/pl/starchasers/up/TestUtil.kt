package pl.starchasers.up

import no.skatteetaten.aurora.mockmvc.extensions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.MethodOrdererContext
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.http.HttpStatus


fun MockMvcData.isSuccess() {
    printResponseBody()
    statusIsOk()
    responseJsonPath("$.success").isTrue()
}

fun MockMvcData.isError(expectedStatus: HttpStatus) {
    printResponseBody()
    status(expectedStatus)
    responseJsonPath("$.success").isFalse()
    responseJsonPath("$.errorMessage").isNotEmpty()
}

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