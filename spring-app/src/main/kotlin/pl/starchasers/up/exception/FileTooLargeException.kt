package pl.starchasers.up.exception

import org.springframework.http.HttpStatus

class FileTooLargeException(): ApplicationException("File too large", HttpStatus.PAYLOAD_TOO_LARGE)