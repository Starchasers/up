package pl.starchasers.up.data.value

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Milliseconds(
        @Column(name="miliseconds")
        val value: Long
) {
    init {
        require(value >= 0)
    }
}