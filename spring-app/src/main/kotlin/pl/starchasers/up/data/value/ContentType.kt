package pl.starchasers.up.data.value

import pl.starchasers.up.util.validate
import javax.persistence.Column
import javax.persistence.Embeddable

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