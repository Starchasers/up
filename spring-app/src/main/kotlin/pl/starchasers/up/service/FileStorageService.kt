package pl.starchasers.up.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.starchasers.up.data.model.FileContent
import pl.starchasers.up.data.model.FileEntry
import pl.starchasers.up.data.value.FileKey
import pl.starchasers.up.data.value.Filename
import pl.starchasers.up.exception.NotFoundException
import pl.starchasers.up.repository.FileEntryRepository
import pl.starchasers.up.repository.UploadRepository
import pl.starchasers.up.util.Util
import java.io.InputStream
import java.time.LocalDateTime

interface FileStorageService {
    fun storeNonPermanentFile(tmpFile: InputStream, filename: Filename): FileKey

    fun getStoredFileRaw(key: FileKey): Pair<FileEntry, InputStream>

    fun deleteFile(fileEntry: FileEntry)
}

//TODO move to app configuration
const val NON_PERMANENT_FILE_KEY_LENGTH = 5
const val PERMANENT_FILE_KEY_LENGTH = 8

@Service
class FileStorageServiceImpl(
        private val uploadRepository: UploadRepository,
        private val fileEntryRepository: FileEntryRepository
) : FileStorageService {


    private val util = Util()

    @Transactional
    override fun storeNonPermanentFile(tmpFile: InputStream, filename: Filename): FileKey {
        val key = FileKey(util.secureReadableRandomString(NON_PERMANENT_FILE_KEY_LENGTH))
        val fileContent = FileContent(key, tmpFile)
        uploadRepository.save(fileContent)

        return key
    }

    override fun getStoredFileRaw(key: FileKey): Pair<FileEntry, InputStream> {
        val fileEntry = fileEntryRepository.findExistingFileByKey(key) ?: throw NotFoundException()

        val upload = uploadRepository.find(key) ?: throw NotFoundException()//TODO handle possible data inconsistency

        return Pair(fileEntry, upload.data)
    }

    override fun deleteFile(fileEntry: FileEntry) {
        uploadRepository.delete(fileEntry.key)
        fileEntryRepository.delete(fileEntry)
    }
}