package pl.starchasers.up.data.dto.upload

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

data class ValidTime(
    val duration: Duration,
    val permanent: Boolean = false
) {
    companion object {
        val PERMANENT = ValidTime(Duration.ZERO, true)
    }
}

@Component
class ExpiresConverter : Converter<String, ValidTime> {

    override fun convert(source: String): ValidTime? {
        if (source.isBlank()) return null

        if (source == "never") return ValidTime.PERMANENT

        if (source.last().isDigit()) {
            return source.toLongOrNull()?.let { ValidTime(Duration.ofMillis(it)) }
        }
        val timeValue = source.substring(0, source.length - 1).toLongOrNull() ?: return null
        if (timeValue <= 0) return null

        return when (source.last()) {
            'h' -> ValidTime(Duration.ofHours(timeValue))
            'd' -> ValidTime(Duration.ofDays(timeValue))
            'w' -> ValidTime(Duration.ofDays(timeValue * 7))
            'm' -> ValidTime(Duration.between(LocalDateTime.now(), LocalDateTime.now().plusMonths(timeValue)))
            'y' -> ValidTime(Duration.between(LocalDateTime.now(), LocalDateTime.now().plusYears(timeValue)))
            else -> null
        }
    }
}
