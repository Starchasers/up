package pl.starchasers.up.data.dto.authentication

import javax.validation.constraints.NotBlank

data class TokenDTO(
        /**
         * JWT token
         */
        @field:NotBlank
        val token: String
)