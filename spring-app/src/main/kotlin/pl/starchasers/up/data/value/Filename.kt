package pl.starchasers.up.data.value

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import pl.starchasers.up.util.validate

@Embeddable
data class Filename(
    @Column(name = "filename", nullable = false, length = 1024)
    val value: String
) {
    init {
        validate(this, Filename::value) {
            check { it.length <= 1024 }
            check { it.isNotBlank() }
        }
    }
}
