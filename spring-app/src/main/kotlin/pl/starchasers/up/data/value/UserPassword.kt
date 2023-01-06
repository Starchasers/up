package pl.starchasers.up.data.value

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import pl.starchasers.up.util.validate

@Embeddable
data class UserPassword(
    @Column(name = "password", length = 160, nullable = false)
    val value: String
) {
    init {
        validate(this, UserPassword::value) {
            check { it.isNotBlank() }
            check { it.length < 160 }
        }
    }
}
