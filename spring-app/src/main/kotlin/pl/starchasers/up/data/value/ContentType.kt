package pl.starchasers.up.data.value

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import pl.starchasers.up.util.validate

@Embeddable
data class ContentType(
    @Column(name = "contentType", nullable = false, length = 256)
    val value: String
) {
    init {
        validate(this, ContentType::value) {
            check { it.length <= 256 }
            check { it.isNotBlank() }
        }
    }
}
