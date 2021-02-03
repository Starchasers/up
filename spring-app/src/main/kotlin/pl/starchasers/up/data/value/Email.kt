package pl.starchasers.up.data.value

import pl.starchasers.up.util.validate
import javax.persistence.Column
import javax.persistence.Embeddable

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
