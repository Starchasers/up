package pl.starchasers.up.data.dto.users

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import pl.starchasers.up.security.Role

@JsonIgnoreProperties(ignoreUnknown = true)
data class UpdateUserDTO(
    val username: String?,
    val email: String?,
    val password: String?,
    val role: Role?,
    val maxTemporaryFileSize: Long?,
    val maxPermanentFileSize: Long?,
    val defaultFileLifetime: Long?,
    val maxFileLifetime: Long?
)
