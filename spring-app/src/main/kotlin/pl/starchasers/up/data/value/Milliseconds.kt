package pl.starchasers.up.data.value

import javax.persistence.AttributeConverter
import javax.persistence.Converter

data class Milliseconds(
        val value: Long
) {
    init {
        require(value >= 0)
    }
}

@Converter
class MillisecondsConverter : AttributeConverter<Milliseconds?, Long?> {
    override fun convertToDatabaseColumn(attribute: Milliseconds?): Long? = attribute?.value

    override fun convertToEntityAttribute(dbData: Long?): Milliseconds? = if (dbData != null) Milliseconds(dbData) else null
}