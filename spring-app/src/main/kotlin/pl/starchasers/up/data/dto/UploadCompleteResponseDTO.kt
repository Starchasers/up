package pl.starchasers.up.data.dto

class UploadCompleteResponseDTO(
        /**
         * File identifier
         */
        val key: String,

        /**
         * File access token, allows modifying file properties after upload
         */
        val accessToken: String
)