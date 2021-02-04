package pl.starchasers.up.data.dto.upload

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.validation.constraints.NotEmpty

@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthorizedOperationDTO(
    /**
     * File access token, obtained during upload
     */
    @field:NotEmpty
    val accessToken: String
)
