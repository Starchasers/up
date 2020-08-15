package pl.starchasers.up.data.value

import javax.persistence.AttributeConverter
import javax.persistence.Converter

data class Miliseconds(
        val value: Long
) {
    init {
        require(value > 0)
    }
}

@Converter
class MilisecondsConverter : AttributeConverter<Miliseconds?, Long?> {
    override fun convertToDatabaseColumn(attribute: Miliseconds?): Long? = attribute?.value

    override fun convertToEntityAttribute(dbData: Long?): Miliseconds? = if (dbData != null) Miliseconds(dbData) else null
}