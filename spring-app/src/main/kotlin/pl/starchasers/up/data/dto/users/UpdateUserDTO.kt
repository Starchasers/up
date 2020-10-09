package pl.starchasers.up.data.dto.users

import pl.starchasers.up.security.Role

data class UpdateUserDTO(
        val email: String?,
        val password: String?,
        val role: Role
)