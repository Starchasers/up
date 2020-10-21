package pl.starchasers.up.data.value

import pl.starchasers.up.util.validate
import javax.persistence.Column
import javax.persistence.Embeddable

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