package pl.starchasers.up.data.dto.users

import pl.starchasers.up.security.Role

data class UserDTO(
        val id: Long,
        val username: String,//TODO use value object
        val email: String?,
        val role: Role
)