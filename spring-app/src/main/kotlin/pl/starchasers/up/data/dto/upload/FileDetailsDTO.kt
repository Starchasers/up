package pl.starchasers.up.data.dto.upload

import pl.starchasers.up.data.model.FileKey
import pl.starchasers.up.data.model.FileName
import pl.starchasers.up.data.model.FileSize
import java.time.Instant

data class FileDetailsDTO(

    /**
     * File id
     */
    val key: FileKey,
    /**
     * Filesystem filename
     */
    val name: FileName,
    /**
     * Will this file expire after some time
     */
    val permanent: Boolean,
    /**
     * File will expire at this date and download link will no longer work. Can be null, if file is permanent
     */
    val expirationDate: Instant?,
    /**
     * File size in bytes
     */
    val size: FileSize,
    /**
     * Content type, eg. "text/plain"
     */
    val type: String
)
