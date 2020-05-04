package pl.starchasers.up.data.dto

import javax.validation.constraints.NotEmpty

class AuthorizedOperationDTO(
        /**
         * File access token, obtained during upload
         */
        @field:NotEmpty
        val accessToken: String
)