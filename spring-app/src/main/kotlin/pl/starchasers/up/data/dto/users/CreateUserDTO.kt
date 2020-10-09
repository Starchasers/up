package pl.starchasers.up.data.dto.users

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import pl.starchasers.up.security.Role

@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateUserDTO(
        val username: String, //TODO use value objects
        val password: String,
        val email: String, //TODO should email be required?
        val role: Role
)