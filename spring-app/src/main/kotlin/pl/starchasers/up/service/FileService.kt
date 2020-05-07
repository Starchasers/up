package pl.starchasers.up.service

import org.springframework.stereotype.Service
import pl.starchasers.up.data.dto.FileDetailsDTO
import pl.starchasers.up.data.dto.UploadCompleteResponseDTO
import pl.starchasers.up.data.model.FileEntry
import pl.starchasers.up.exception.NotFoundException
import pl.starchasers.up.repository.FileEntryRepository
import pl.starchasers.up.util.Util
import java.io.InputStream
import java.lang.IllegalStateException
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.transaction.Transactional

interface FileService {

    fun createFile(tmpFile: InputStream, filename: String, contentType: String, size: Long): UploadCompleteResponseDTO

    fun verifyFileAccess(fileEntry: FileEntry, accessToken: String): Boolean

    fun verifyFileAccess(fileKey: String, accessToken: String): Boolean

    fun findFileEntry(fileKey: String): FileEntry?

    fun getFileDetails(fileKey: String): FileDetailsDTO
}

@Service
class FileServiceImpl(
        private val fileStorageService: FileStorageService,
        private val fileEntryRepository: FileEntryRepository
) : FileService {

    private val util = Util()

    @Transactional
    override fun createFile(
            tmpFile: InputStream,
            filename: String,
            contentType: String,
            size: Long
    ): UploadCompleteResponseDTO {
        val key = fileStorageService.storeNonPermanentFile(tmpFile, filename)
        //TODO check key already used
        val accessToken = generateFileAccessToken()
        val toDeleteDate = Timestamp.valueOf(LocalDateTime.now().plusDays(1))
        val fileEntry = FileEntry(0,
                key,
                filename,
                contentType.let { if (contentType.isBlank()) "application/octet-stream" else contentType },
                null,
                false,
                Timestamp.valueOf(LocalDateTime.now()),
                toDeleteDate,
                false,
                accessToken,
                size)

        fileEntryRepository.save(fileEntry)


        return UploadCompleteResponseDTO(key, accessToken, toDeleteDate.toLocalDateTime())
    }

    override fun verifyFileAccess(fileEntry: FileEntry, accessToken: String): Boolean =
            fileEntry.accessToken.isNotBlank() && fileEntry.accessToken == accessToken

    override fun verifyFileAccess(fileKey: String, accessToken: String): Boolean =
            fileEntryRepository
                    .findExistingFileByKey(fileKey)
                    ?.let { it.accessToken.isNotBlank() && it.accessToken == accessToken } ?: false

    override fun findFileEntry(fileKey: String): FileEntry? = fileEntryRepository.findExistingFileByKey(fileKey)

    override fun getFileDetails(fileKey: String): FileDetailsDTO =
            fileEntryRepository.findExistingFileByKey(fileKey)?.let {
                FileDetailsDTO(
                        it.key,
                        it.filename,
                        it.permanent,
                        if (it.permanent) it.toDeleteDate?.toLocalDateTime()
                                ?: throw IllegalStateException("Not permanent file without delete date! FileKey: ${it.key}")
                        else null,
                        it.size,
                        it.contentType)
            } ?: throw NotFoundException()


    private fun generateFileAccessToken(): String = util.secureAlphanumericRandomString(128)

}