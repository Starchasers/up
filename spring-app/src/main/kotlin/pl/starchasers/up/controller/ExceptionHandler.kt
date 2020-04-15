package pl.starchasers.up.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pl.starchasers.up.util.ApplicationException
import pl.starchasers.up.util.BasicErrorResponseDTO

@ControllerAdvice
class ExceptionHandler() {


    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(applicationException: ApplicationException): ResponseEntity<BasicErrorResponseDTO> =
        ResponseEntity(BasicErrorResponseDTO(applicationException.errorMessage), applicationException.responseStatus)

    @ExceptionHandler(MethodArgumentNotValidException::class, HttpMessageNotReadableException::class)
    fun handleValidationErrors(): ResponseEntity<BasicErrorResponseDTO> =
        ResponseEntity(BasicErrorResponseDTO("Bad request."), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(): ResponseEntity<BasicErrorResponseDTO> =
        ResponseEntity(BasicErrorResponseDTO("Access denied"), HttpStatus.UNAUTHORIZED)

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleUnsupportedMethodException(): ResponseEntity<BasicErrorResponseDTO> =
        ResponseEntity(BasicErrorResponseDTO("Not Found"), HttpStatus.NOT_FOUND)

    @ExceptionHandler(Exception::class)
    fun handleAll(exception: Exception): ResponseEntity<BasicErrorResponseDTO> {
        exception.printStackTrace()
        return ResponseEntity(BasicErrorResponseDTO("Internal sever error"), HttpStatus.INTERNAL_SERVER_ERROR)
    }

}