package pl.starchasers.up.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import pl.starchasers.up.data.dto.upload.FileDetailsDTO
import pl.starchasers.up.data.dto.upload.UploadCompleteResponseDTO
import pl.starchasers.up.data.dto.upload.ValidTime
import pl.starchasers.up.data.model.ConfigurationKey.ANONYMOUS_MAX_FILE_SIZE
import pl.starchasers.up.data.model.FileEntry
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.*
import pl.starchasers.up.exception.BadRequestException
import pl.starchasers.up.exception.FileTooLargeException
import pl.starchasers.up.exception.NotFoundException
import pl.starchasers.up.repository.FileEntryRepository
import pl.starchasers.up.util.Util
import java.io.InputStream
import java.time.LocalDateTime
import javax.transaction.Transactional

interface FileService {

    fun createFile(
        tmpFile: InputStream,
        filename: Filename,
        contentType: ContentType,
        size: FileSize,
        user: User?,
        validTime: ValidTime? = null
    ): UploadCompleteResponseDTO

    fun verifyFileAccess(fileEntry: FileEntry, accessToken: FileAccessToken?, user: User?): Boolean

    fun verifyFileAccess(fileKey: FileKey, accessToken: FileAccessToken?, user: User?): Boolean

    fun findFileEntry(fileKey: FileKey): FileEntry?

    fun getFileDetails(fileKey: FileKey): FileDetailsDTO

    fun deleteFile(fileEntry: FileEntry)

    fun getUploadHistory(user: User, pageable: Pageable): Page<FileEntry>
}

@Service
class FileServiceImpl(
    private val fileStorageService: FileStorageService,
    private val fileEntryRepository: FileEntryRepository,
    private val configurationService: ConfigurationService,
    private val charsetDetectionService: CharsetDetectionService
) : FileService {

    @Value("\${up.max-file-size}")
    private val maxUploadSize: Long = 0

    private val util = Util()

    @Transactional
    override fun createFile(
        tmpFile: InputStream,
        filename: Filename,
        contentType: ContentType,
        size: FileSize,
        user: User?,
        validTime: ValidTime?
    ): UploadCompleteResponseDTO {
        checkSizeLimit(user, size)

        val toDeleteDate = getDeleteDate(user, validTime)
        checkDeleteDate(toDeleteDate, user)

        val actualContentType = getContentType(contentType, tmpFile)

        val key = fileStorageService.storeNonPermanentFile(tmpFile, filename)
        // TODO check key already used

        val accessToken = generateFileAccessToken()
        val fileEntry = FileEntry(
            0,
            key,
            filename,
            actualContentType,
            null,
            false,
            LocalDateTime.now(),
            toDeleteDate,
            false,
            accessToken,
            size,
            user
        )

        fileEntryRepository.save(fileEntry)

        return UploadCompleteResponseDTO(key.value, accessToken.value, toDeleteDate)
    }

    private fun getContentType(declaredContentType: ContentType, tmpFile: InputStream): ContentType = when {
        declaredContentType.value.isBlank() -> ContentType("application/octet-stream")
        declaredContentType.value == "text/plain" -> ContentType(
            "text/plain; charset=" + charsetDetectionService.detect(
                tmpFile
            )
        )
        else -> declaredContentType
    }

    private fun checkSizeLimit(user: User?, size: FileSize) {
        val personalLimit: FileSize = user?.maxTemporaryFileSize
            ?: FileSize(configurationService.getConfigurationOption(ANONYMOUS_MAX_FILE_SIZE).toLong())

        if (size > personalLimit) throw FileTooLargeException()
    }

    private fun getDeleteDate(user: User?, validTime: ValidTime?): LocalDateTime? = when {
        validTime == null -> getDefaultDeleteDate(user)
        validTime.permanent -> null
        else -> LocalDateTime.now().plus(validTime.duration)
    }

    private fun getDefaultDeleteDate(user: User?): LocalDateTime? = when {
        user == null -> configurationService.getAnonymousMaxFileLifetime().fromNow()
        user.defaultFileLifetime.value == 0L -> null
        else -> user.defaultFileLifetime.fromNow()
    }

    private fun getMaxDeleteDate(user: User?): LocalDateTime? = when {
        user == null -> configurationService.getAnonymousMaxFileLifetime().fromNow()
        user.maxFileLifetime.value == 0L -> null
        else -> user.maxFileLifetime.fromNow()
    }

    private fun checkDeleteDate(deleteDate: LocalDateTime?, user: User?) {
        val maxDate = getMaxDeleteDate(user) ?: return // skip check if permanent allowed

        if (deleteDate == null) throw BadRequestException("Permanent files not allowed.")
        if (deleteDate.isAfter(maxDate)) throw BadRequestException("Expiration time too big.")
    }

    override fun verifyFileAccess(fileEntry: FileEntry, accessToken: FileAccessToken?, user: User?): Boolean {
        return (user != null && fileEntry.owner == user) || fileEntry.accessToken == accessToken
    }

    override fun verifyFileAccess(fileKey: FileKey, accessToken: FileAccessToken?, user: User?): Boolean =
        fileEntryRepository
            .findExistingFileByKey(fileKey)
            ?.let { verifyFileAccess(it, accessToken, user) } ?: false

    override fun findFileEntry(fileKey: FileKey): FileEntry? = fileEntryRepository.findExistingFileByKey(fileKey)

    override fun getFileDetails(fileKey: FileKey): FileDetailsDTO =
        fileEntryRepository.findExistingFileByKey(fileKey)?.let {
            FileDetailsDTO(
                it.key.value,
                it.filename.value,
                it.permanent,
                if (!it.permanent) it.toDeleteDate
                    ?: throw IllegalStateException("Temporary file without delete date! FileKey: ${it.key}")
                else null,
                it.size.value,
                it.contentType.value
            )
        } ?: throw NotFoundException()

    override fun deleteFile(fileEntry: FileEntry) {
        fileStorageService.deleteFile(fileEntry)
    }

    override fun getUploadHistory(user: User, pageable: Pageable): Page<FileEntry> {
        return fileEntryRepository.findAllByOwner(user, pageable)
    }

    private fun generateFileAccessToken(): FileAccessToken = FileAccessToken(util.secureAlphanumericRandomString(128))
}
