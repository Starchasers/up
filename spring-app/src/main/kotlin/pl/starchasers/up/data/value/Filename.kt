package pl.starchasers.up.data.value

import pl.starchasers.up.util.validate
import javax.persistence.Column
import javax.persistence.Embeddable

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