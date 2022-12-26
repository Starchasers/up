package pl.starchasers.up.data.value

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import pl.starchasers.up.util.validate

@Embeddable
data class Email(
    @Column(name = "email", length = 64)
    val value: String
) {
    init {
        validate(this, Email::value) {
            check { it.isNotBlank() }
            check { it.length <= 64 }
        }
    }
}

fun String?.toEmail(): Email? = this?.let { Email(it) }
