package pl.starchasers.up.data.dto.authentication

import pl.starchasers.up.security.Role

data class LoginResponseDTO(
    /**
     * JWT refresh token
     */
    val token: String,
    /**
     * Role of the logged in user
     */
    val role: Role
)
