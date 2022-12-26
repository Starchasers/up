package pl.starchasers.up.data.value

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import pl.starchasers.up.util.validate

@Embeddable
data class RefreshTokenId(
    @Column(name = "refreshToken", nullable = false, unique = true, updatable = false)
    val value: String
) {
    init {
        validate(this, RefreshTokenId::value) {
            check { it.isNotBlank() }
        }
    }
}
