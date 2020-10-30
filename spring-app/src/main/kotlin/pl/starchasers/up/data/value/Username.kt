package pl.starchasers.up.data.value

import pl.starchasers.up.util.runOrNull
import pl.starchasers.up.util.validate
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Username(
        @Column(name = "username", length = 32, unique = true, updatable = true, nullable = false)
        val value: String
) {
    init {
        validate(this, Username::value) {
            check { it.isNotBlank() }
            check { it.length < 32 }
            check { it.all { character -> character.isLetterOrDigit() } }
        }
    }
}


fun String?.toUsername(): Username? = this.runOrNull { Username(it) }
