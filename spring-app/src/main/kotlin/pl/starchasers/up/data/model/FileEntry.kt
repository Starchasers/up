package pl.starchasers.up.data.model

import org.ktorm.schema.*
import org.springframework.util.MimeType
import java.time.Instant

data class FileEntry(
    val id: FileId,
    var accessToken: FileAccessToken?,
    var contentType: MimeType,
    var createdAt: Instant,
    var encrypted: Boolean,
    var filename: FileName,
    var key: FileKey,
    var password: FilePassword?,
    var size: FileSize,
    var toDeleteAt: Instant?
)

object FileEntries : Table<Nothing>("file_entry") {
    val id = long("id").primaryKey()
    val accessToken = text("file_access_token")
    val contentType = text("content_type")
    val createdAt = timestamp("created_at")
    val encrypted = boolean("encrypted")
    val filename = text("filename")
    val key = text("file_key")
    val password = text("file_password")
    val size = long("file_size")
    val deleteAt = timestamp("to_delete_at")
}
