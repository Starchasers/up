package pl.starchasers.up.data.value

import pl.starchasers.up.util.validate
import java.time.Duration
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embeddable

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

    fun fromNow(): LocalDateTime {
        return LocalDateTime.now().plus(Duration.ofMillis(value))
    }
}

fun Long?.toMilliseconds(): Milliseconds? = this?.let { Milliseconds(it) }
