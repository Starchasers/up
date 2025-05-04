package pl.starchasers.up.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.starchasers.up.data.dto.upload.FileDetailsDTO
import pl.starchasers.up.data.dto.upload.UploadCompleteResponseDTO
import pl.starchasers.up.data.model.*
import pl.starchasers.up.data.model.ConfigurationKey.ANONYMOUS_MAX_FILE_SIZE
import pl.starchasers.up.exception.FileTooLargeException
import pl.starchasers.up.exception.NotFoundException
import pl.starchasers.up.repository.FileEntryRepository
import pl.starchasers.up.util.Util
import java.io.InputStream
import java.time.Instant
import java.time.temporal.ChronoUnit

interface FileService {

    fun createFile(
        tmpFile: InputStream,
        filename: FileName,
        contentType: String,
        size: FileSize
    ): UploadCompleteResponseDTO

    fun verifyFileAccess(fileEntry: FileEntry, accessToken: FileAccessToken): Boolean

    fun verifyFileAccess(fileKey: FileKey, accessToken: FileAccessToken): Boolean

    fun findFileEntry(fileKey: FileKey): FileEntry?

    fun getFileDetails(fileKey: FileKey): FileDetailsDTO

    fun deleteFile(fileEntry: FileEntry)
}

@Service
class FileServiceImpl(
    private val fileStorageService: FileStorageService,
    private val fileEntryRepository: FileEntryRepository,
    private val configurationService: ConfigurationService,
    private val charsetDetectionService: CharsetDetectionService
) : FileService {

    private val util = Util()

    @Transactional
    override fun createFile(
        tmpFile: InputStream,
        filename: FileName,
        contentType: String,
        size: FileSize
    ): UploadCompleteResponseDTO {
        val actualContentType = when {
            contentType.isBlank() -> "application/octet-stream"
            contentType == "text/plain" -> "text/plain;charset=" + charsetDetectionService.detect(tmpFile)
            else -> contentType
        }
        val personalLimit: Long = configurationService.getConfigurationOption(ANONYMOUS_MAX_FILE_SIZE).toLong()

        if (size.value > personalLimit) throw FileTooLargeException()

        val key = fileStorageService.storeNonPermanentFile(tmpFile, filename)
        // TODO check key already used
        val accessToken = generateFileAccessToken()
        val toDeleteAt = Instant.now().plus(1, ChronoUnit.DAYS)

        fileEntryRepository.insert {
            set(it.accessToken, accessToken)
            set(it.contentType, actualContentType)
            set(it.createdAt, Instant.now())
            set(it.encrypted, false)
            set(it.filename, filename.value)
            set(it.key, key.value)
            set(it.password, null)
            set(it.size, size.value)
            set(it.deleteAt, toDeleteAt)
        }

        return UploadCompleteResponseDTO(key, accessToken, toDeleteAt)
    }

    override fun verifyFileAccess(fileEntry: FileEntry, accessToken: FileAccessToken): Boolean {
        return (fileEntry.accessToken != null) && fileEntry.accessToken == accessToken
    }

    override fun verifyFileAccess(fileKey: FileKey, accessToken: FileAccessToken): Boolean =
        fileEntryRepository
            .findExistingFileByKey(fileKey)
            ?.let { verifyFileAccess(it, accessToken) } ?: throw NotFoundException()

    override fun findFileEntry(fileKey: FileKey): FileEntry? = fileEntryRepository.findExistingFileByKey(fileKey)

    override fun getFileDetails(fileKey: FileKey): FileDetailsDTO =
        fileEntryRepository.findExistingFileByKey(fileKey)?.let {
            FileDetailsDTO(
                it.key,
                it.filename,
                it.toDeleteAt == null,
                it.toDeleteAt,
                it.size,
                it.contentType.toString()
            )
        } ?: throw NotFoundException()

    override fun deleteFile(fileEntry: FileEntry) {
        fileStorageService.deleteFile(fileEntry)
    }

    private fun generateFileAccessToken(): String = util.secureAlphanumericRandomString(128)
}
