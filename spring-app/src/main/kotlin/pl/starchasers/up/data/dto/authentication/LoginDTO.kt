package pl.starchasers.up.data.dto.authentication

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.validation.constraints.NotBlank

@JsonIgnoreProperties(ignoreUnknown = true)
data class LoginDTO(
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
