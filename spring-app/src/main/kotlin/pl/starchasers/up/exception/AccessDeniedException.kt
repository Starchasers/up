package pl.starchasers.up.exception

import org.springframework.http.HttpStatus

class AccessDeniedException : ApplicationException("Access denied.", HttpStatus.FORBIDDEN)