package pl.starchasers.up.data.value

import pl.starchasers.up.util.validate
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class RefreshTokenId(
        @Column(name = "refreshToken", nullable = false, unique = true, updatable = false)
        val value: String
) {
    init {
        validate(this, RefreshTokenId::value){
            check { it.isNotBlank() }
        }
    }
}