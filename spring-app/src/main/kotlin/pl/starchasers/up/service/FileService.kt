package pl.starchasers.up.service

import org.springframework.stereotype.Service
import pl.starchasers.up.data.dto.UploadCompleteResponseDTO
import pl.starchasers.up.data.model.FileEntry
import pl.starchasers.up.repository.FileEntryRepository
import pl.starchasers.up.util.Util
import java.io.InputStream
import java.time.LocalDateTime
import javax.transaction.Transactional

interface FileService {

    fun createFile(tmpFile: InputStream, filename: String, contentType: String): UploadCompleteResponseDTO

    fun verifyFileAccess(fileEntry: FileEntry, accessToken: String): Boolean

    fun verifyFileAccess(fileKey: String, accessToken: String): Boolean

    fun findFileEntry(fileKey: String): FileEntry?

}

@Service
class FileServiceImpl(
        private val fileStorageService: FileStorageService,
        private val fileEntryRepository: FileEntryRepository
) : FileService {

    private val util = Util()

    @Transactional
    override fun createFile(tmpFile: InputStream, filename: String, contentType: String): UploadCompleteResponseDTO {
        val key = fileStorageService.storeNonPermanentFile(tmpFile, filename)
        //TODO check key already used

        val accessToken = generateFileAccessToken()
        val fileEntry = FileEntry(0,
                key,
                filename,
                contentType.let { if (contentType.isBlank()) "application/octet-stream" else contentType },
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                false,
                accessToken)

        fileEntryRepository.save(fileEntry)


        return UploadCompleteResponseDTO(key, accessToken)
    }

    override fun verifyFileAccess(fileEntry: FileEntry, accessToken: String): Boolean =
            fileEntry.accessToken.isNotBlank() && fileEntry.accessToken == accessToken

    override fun verifyFileAccess(fileKey: String, accessToken: String): Boolean =
            fileEntryRepository
                    .findExistingFileByKey(fileKey)
                    ?.let { it.accessToken.isNotBlank() && it.accessToken == accessToken } ?: false

    override fun findFileEntry(fileKey: String): FileEntry? = fileEntryRepository.findExistingFileByKey(fileKey)


    private fun generateFileAccessToken(): String = util.secureAlphanumericRandomString(128)

}