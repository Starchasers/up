package pl.starchasers.up.data.value

import pl.starchasers.up.util.validate
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class FileKey(
        @Column(name = "fileKey", nullable = false, length = 32)
        val value: String
) {
    init {
        validate(this, FileKey::value) {
            check { it.isNotEmpty() }
            check { it.all { character -> character.isLetterOrDigit() } }
            check { it.length <= 32 }
        }
    }
}