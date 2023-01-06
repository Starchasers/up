package pl.starchasers.up.data.value

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import pl.starchasers.up.util.validate

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

fun Long?.toFileSize(): FileSize? = this?.let { FileSize(it) }
