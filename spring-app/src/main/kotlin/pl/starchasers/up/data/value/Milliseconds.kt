package pl.starchasers.up.data.value

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import pl.starchasers.up.util.validate

@Embeddable
data class Milliseconds(
    @Column(name = "miliseconds")
    val value: Long
) {
    init {
        validate(this, Milliseconds::value) {
            check { it >= 0 }
        }
    }
}

fun Long?.toMilliseconds(): Milliseconds? = this?.let { Milliseconds(it) }
