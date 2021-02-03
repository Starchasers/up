package pl.starchasers.up.exception

import org.springframework.http.HttpStatus

class BadRangeException(
    val fileSize: Long
) : ApplicationException("Range Not Satisfiable", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
