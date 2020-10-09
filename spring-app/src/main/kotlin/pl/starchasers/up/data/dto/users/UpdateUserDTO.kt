package pl.starchasers.up.data.dto.users

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import pl.starchasers.up.security.Role

@JsonIgnoreProperties(ignoreUnknown = true)
data class UpdateUserDTO(
        val email: String?,
        val password: String?,
        val role: Role
)