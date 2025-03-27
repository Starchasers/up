package pl.starchasers.up.data.dto.upload

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.validation.constraints.NotEmpty
import pl.starchasers.up.data.model.FileAccessToken

@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthorizedOperationDTO(
    /**
     * File access token, obtained during upload
     */
    @field:NotEmpty
    val accessToken: FileAccessToken
)
