package pl.starchasers.up.data.value

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import pl.starchasers.up.util.validate

@Embeddable
data class FileAccessToken(
    @Column(name = "fileAccessToken", length = 128)
    val value: String
) {
    init {
        validate(this, FileAccessToken::value) {
            check("Token too long.") { it.length <= 128 }
            check("Token cannot be blank.") { it.isNotBlank() }
        }
    }
}
