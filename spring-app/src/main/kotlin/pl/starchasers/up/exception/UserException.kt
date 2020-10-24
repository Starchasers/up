package pl.starchasers.up.exception

import org.springframework.http.HttpStatus

class UserException(reason: String = "Access denied.") : ApplicationException(reason, HttpStatus.FORBIDDEN)