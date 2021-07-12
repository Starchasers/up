package pl.starchasers.up.controller

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestCookieException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MultipartException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import pl.starchasers.up.exception.ApplicationException
import pl.starchasers.up.exception.BadRangeException
import pl.starchasers.up.util.BasicErrorResponseDTO

@ControllerAdvice
class ExceptionHandler() {

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(applicationException: ApplicationException): ResponseEntity<BasicErrorResponseDTO> =
        ResponseEntity(BasicErrorResponseDTO(applicationException.errorMessage), applicationException.responseStatus)

    @ExceptionHandler(BadRangeException::class)
    fun handleBadRangeException(badRangeException: BadRangeException): ResponseEntity<BasicErrorResponseDTO> {
        val httpHeaders = HttpHeaders()
        httpHeaders.set(HttpHeaders.CONTENT_RANGE, "bytes */${badRangeException.fileSize}")
        return ResponseEntity(
            BasicErrorResponseDTO(badRangeException.errorMessage),
            httpHeaders,
            badRangeException.responseStatus
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMessageNotReadable(exception: HttpMessageNotReadableException): ResponseEntity<BasicErrorResponseDTO> {
        val message = if (exception.cause is MissingKotlinParameterException) {
            "Bad request. Missing required parameter '${(exception.cause as MissingKotlinParameterException).parameter.name}'."
        } else "Bad request."

        return ResponseEntity(BasicErrorResponseDTO(message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(exception: MethodArgumentNotValidException): ResponseEntity<BasicErrorResponseDTO> =
        ResponseEntity(
            BasicErrorResponseDTO(
                "Bad request. Missing or invalid parameter '${exception.parameter.parameterName}.'"
            ),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleArgumentTypeMismatch(exception: MethodArgumentTypeMismatchException): ResponseEntity<BasicErrorResponseDTO> =
        ResponseEntity(
            BasicErrorResponseDTO("Bad request. Malformed parameter ${exception.parameter}"),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(): ResponseEntity<BasicErrorResponseDTO> =
        ResponseEntity(BasicErrorResponseDTO("Access denied."), HttpStatus.FORBIDDEN)

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleUnsupportedMethodException(): ResponseEntity<BasicErrorResponseDTO> =
        ResponseEntity(BasicErrorResponseDTO("Not Found."), HttpStatus.NOT_FOUND)

    @ExceptionHandler(MissingServletRequestPartException::class)
    fun handleMissingServletRequestException(): ResponseEntity<BasicErrorResponseDTO> =
        ResponseEntity(BasicErrorResponseDTO("Bad Request. Malformed multipart request."), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(exception: MissingServletRequestParameterException): ResponseEntity<BasicErrorResponseDTO> =
        ResponseEntity(
            BasicErrorResponseDTO(
                "Bad Request. Missing required parameter '${exception.parameterName}'."
            ),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(MissingRequestCookieException::class)
    fun handleMissingCookieException(exception: MissingRequestCookieException): ResponseEntity<BasicErrorResponseDTO> =
        ResponseEntity(
            BasicErrorResponseDTO("Bad Request. Cookie \"${exception.cookieName}\" not present."),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(MultipartException::class)
    fun handleMultipartException(): ResponseEntity<BasicErrorResponseDTO> =
        ResponseEntity(BasicErrorResponseDTO("Bad Request. Malformed multipart request."), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(Exception::class)
    fun handleAll(exception: Exception): ResponseEntity<BasicErrorResponseDTO> {
        exception.printStackTrace()
        return ResponseEntity(BasicErrorResponseDTO("Internal sever error."), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
