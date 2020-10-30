package pl.starchasers.up.data.value

import pl.starchasers.up.util.runOrNull
import pl.starchasers.up.util.validate
import javax.persistence.AttributeConverter
import javax.persistence.Column
import javax.persistence.Converter
import javax.persistence.Embeddable


@Embeddable
data class FileSize(
        @Column(name = "fileSize")
        val value: Long
) {
    init {
        validate(this, FileSize::value) {
            check { it >= 0 }
        }
    }

    operator fun compareTo(fileSize: FileSize): Int {
        return (this.value - fileSize.value).toInt()
    }
}

fun Long?.toFileSize(): FileSize? = this.runOrNull { FileSize(it) }