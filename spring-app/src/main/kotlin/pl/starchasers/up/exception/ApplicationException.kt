package pl.starchasers.up.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

open class ApplicationException(
        val errorMessage: String = "Bad request.",
        val responseStatus: HttpStatus = HttpStatus.BAD_REQUEST
) :
    RuntimeException(errorMessage) {
}