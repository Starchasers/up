package pl.starchasers.up.exception

import org.springframework.http.HttpStatus

class AccessDeniedException(reason: String = "Access denied.") : ApplicationException(reason, HttpStatus.FORBIDDEN)