package pl.starchasers.up.exception

import org.springframework.http.HttpStatus

class InvalidCredentialsException(reason: String = "Invalid credentials.") :
    ApplicationException(reason, HttpStatus.UNAUTHORIZED)
