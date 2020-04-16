package pl.starchasers.up.service

import org.springframework.stereotype.Service
import pl.starchasers.up.data.model.FileContent
import pl.starchasers.up.data.model.FileEntry
import pl.starchasers.up.repository.UploadRepository
import pl.starchasers.up.util.Util
import java.io.File
import java.io.IOError
import java.io.IOException
import java.io.InputStream
import java.time.LocalDateTime

interface FileStorageService {
    fun storeTemporaryFile(inputStream: InputStream): File

    fun storeNonPermanentFile(tmpFile: File, params: Map<String, String>, filename: String): String
}

//TODO move to app configuration
const val NON_PERMANENT_FILE_KEY_LENGTH = 5
const val PERMANENT_FILE_KEY_LENGTH = 8

@Service
class FileStorageServiceImpl(private val uploadRepository: UploadRepository) : FileStorageService {


    private val util = Util()

    override fun storeTemporaryFile(inputStream: InputStream): File {
        return uploadRepository.saveTmp(inputStream)
    }

    override fun storeNonPermanentFile(tmpFile: File, params: Map<String, String>, filename: String): String {
        val key = util.secureRandomString(NON_PERMANENT_FILE_KEY_LENGTH)
        //TODO check key already used
        val fileEntry = FileEntry(0,
                key,
                filename,
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                false)
        val tmpFileInputStream = tmpFile.inputStream()

        val fileContent = FileContent(key, tmpFileInputStream)
        uploadRepository.save(fileContent)


        //cleanup
        tmpFileInputStream.close()
        try {
            tmpFile.delete()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return key
    }


}