package pl.starchasers.up.data.value

import pl.starchasers.up.util.validate
import javax.persistence.Column
import javax.persistence.Embeddable

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