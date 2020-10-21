package pl.starchasers.up.data.value

import pl.starchasers.up.util.validate
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class ContentType(
        @Column(name = "contentType", nullable = false, length = 32)
        val value: String
) {
    init {
        validate(this, ContentType::value) {
            check { it.length <= 32 }
            check { it.isNotBlank() }
        }
    }
}