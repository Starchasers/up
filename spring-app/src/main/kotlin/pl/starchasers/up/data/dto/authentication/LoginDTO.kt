package pl.starchasers.up.data.dto.authentication

import javax.validation.constraints.NotBlank

class LoginDTO(
        /**
         * User's username
         */
        @field:NotBlank
        val username: String,
        /**
         * User's password, in plaintext
         */
        @field:NotBlank
        val password: String
)