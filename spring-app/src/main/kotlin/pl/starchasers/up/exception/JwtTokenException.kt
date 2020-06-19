package pl.starchasers.up.exception

import org.springframework.http.HttpStatus

class JwtTokenException(reason: String) : ApplicationException(reason, HttpStatus.FORBIDDEN)