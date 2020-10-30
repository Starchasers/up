package pl.starchasers.up.data.dto.users

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import pl.starchasers.up.security.Role

@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateUserDTO(
        val username: String,
        val password: String,
        val email: String?,
        val role: Role
)