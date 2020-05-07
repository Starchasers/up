package pl.starchasers.up.data.dto

import java.sql.Timestamp
import java.time.LocalDateTime

class UploadCompleteResponseDTO(
        /**
         * File identifier
         */
        val key: String,

        /**
         * File access token, allows modifying file properties after upload
         */
        val accessToken: String,

        /**
         * Date and time after which file will be deleted
         */
        val toDelete: LocalDateTime
)