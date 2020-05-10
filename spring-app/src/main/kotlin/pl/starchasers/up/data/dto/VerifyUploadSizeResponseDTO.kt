package pl.starchasers.up.data.dto

class VerifyUploadSizeResponseDTO(
        /**
         * True, if file can be uploaded
         */
        val valid: Boolean,

        /**
         * Maximum allowed upload size in KB
         */
        val maxUploadSize: Long
)