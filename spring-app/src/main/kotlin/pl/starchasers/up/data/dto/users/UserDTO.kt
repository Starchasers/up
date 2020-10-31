package pl.starchasers.up.data.dto.users

import pl.starchasers.up.data.model.User
import pl.starchasers.up.security.Role

data class UserDTO(
        val id: Long,
        val username: String,
        val email: String?,
        val role: Role,
        val maxTemporaryFileSize: Long,
        val maxPermanentFileSize: Long,
        val defaultFileLifetime: Long,
        val maxFileLifetime: Long
)

fun User.toUserDTO() = UserDTO(id,
        username.value,
        email?.value ?: "",
        role,
        maxTemporaryFileSize.value,
        maxPermanentFileSize.value,
        defaultFileLifetime.value,
        maxFileLifetime.value)