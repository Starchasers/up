package pl.starchasers.up.data.value

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import pl.starchasers.up.util.validate

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
