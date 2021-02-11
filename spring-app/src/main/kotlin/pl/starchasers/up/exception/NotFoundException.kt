package pl.starchasers.up.exception

import org.springframework.http.HttpStatus

open class NotFoundException : ApplicationException("Not found.", HttpStatus.NOT_FOUND)

class NotFoundUIException : NotFoundException()
