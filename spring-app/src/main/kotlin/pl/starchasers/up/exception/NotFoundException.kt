package pl.starchasers.up.exception

import org.springframework.http.HttpStatus

class NotFoundException : ApplicationException("Not found.", HttpStatus.NOT_FOUND)
