package pl.starchasers.up.data.dto.upload

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import pl.starchasers.up.exception.BadRequestException
import java.time.Duration
import java.time.LocalDateTime

data class FileLifetime(
    val duration: Duration,
    val permanent: Boolean = false
) {
    companion object {
        val PERMANENT = FileLifetime(Duration.ZERO, true)
    }
}

@Component
class FileLifetimeConverter : Converter<String, FileLifetime> {

    private val exceptionMessage = "Malformed expires parameter."

    override fun convert(source: String): FileLifetime? {
        if (source.isBlank()) throw BadRequestException(exceptionMessage)

        if (source == "never") return FileLifetime.PERMANENT

        if (source.last().isDigit()) {
            val timeValue = source.toLongOrNull() ?: throw BadRequestException(exceptionMessage)
            if (timeValue < 0) throw BadRequestException(exceptionMessage)
            return source.toLongOrNull()?.let { FileLifetime(Duration.ofMillis(it)) }
        }
        val timeValue =
            source.substring(0, source.length - 1).toLongOrNull() ?: throw BadRequestException(exceptionMessage)
        if (timeValue <= 0) throw BadRequestException(exceptionMessage)

        return when (source.last()) {
            'h' -> FileLifetime(Duration.ofHours(timeValue))
            'd' -> FileLifetime(Duration.ofDays(timeValue))
            'w' -> FileLifetime(Duration.ofDays(timeValue * 7))
            'm' -> FileLifetime(Duration.between(LocalDateTime.now(), LocalDateTime.now().plusMonths(timeValue)))
            'y' -> FileLifetime(Duration.between(LocalDateTime.now(), LocalDateTime.now().plusYears(timeValue)))
            else -> throw BadRequestException(exceptionMessage)
        }
    }
}
