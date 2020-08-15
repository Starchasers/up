package pl.starchasers.up.data.value

import javax.persistence.AttributeConverter
import javax.persistence.Converter


data class FileSize(
        val value: Long
) {
    init {
        require(value >= 0)
    }
}

@Converter
class FileSizeConverter : AttributeConverter<FileSize?, Long?> {
    override fun convertToDatabaseColumn(attribute: FileSize?): Long? = attribute?.value

    override fun convertToEntityAttribute(dbData: Long?): FileSize? = if (dbData != null) FileSize(dbData) else null
}