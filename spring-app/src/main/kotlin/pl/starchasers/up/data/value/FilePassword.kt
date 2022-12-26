package pl.starchasers.up.data.value

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import pl.starchasers.up.util.validate

@Embeddable
data class FilePassword(
    @Column(name = "filePassword", length = 255)
    val value: String
) {
    init {
        validate(this, FilePassword::value) {
            check { it.length <= 255 }
        }
    }
}
