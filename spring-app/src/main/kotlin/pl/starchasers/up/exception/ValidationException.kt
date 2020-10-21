package pl.starchasers.up.exception

import org.springframework.http.HttpStatus

class ValidationException(message: String) : ApplicationException(message, HttpStatus.BAD_REQUEST)