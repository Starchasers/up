package pl.starchasers.up.data.dto.authentication

import jakarta.validation.constraints.NotBlank

data class TokenDTO(
    /**
     * JWT token
     */
    @field:NotBlank
    val token: String
)
