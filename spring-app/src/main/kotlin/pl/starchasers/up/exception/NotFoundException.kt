package pl.starchasers.up.exception

import org.springframework.http.HttpStatus

class NotFoundException : ApplicationException("File not found.", HttpStatus.NOT_FOUND)