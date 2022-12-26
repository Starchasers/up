package pl.starchasers.up.repository

import jakarta.annotation.PostConstruct
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import pl.starchasers.up.data.model.FileContent
import pl.starchasers.up.data.value.FileKey
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Paths

interface UploadRepository {
    fun save(fileContent: FileContent)

    fun find(key: FileKey): FileContent?

    fun delete(key: FileKey)

    fun exists(key: FileKey): Boolean
}

@Repository
class UploadRepositoryImpl() : UploadRepository {
    @Value("\${up.datastore}")
    private lateinit var dataStorePath: String

    @PostConstruct
    fun initializeDataStore() {
        createDataStore()
    }

    private fun createDataStore() {
        val dataStoreRootDir = File(dataStorePath)
        if (dataStoreRootDir.exists()) {
            if (dataStoreRootDir.isDirectory) return
            else throw IllegalStateException("DataStore \"$dataStorePath\" already exists and is not a directory")
        } else {
            dataStoreRootDir.mkdirs()
        }
    }

    override fun save(fileContent: FileContent) {
        val file = getFileFromKey(fileContent.key)

        if (file.exists()) {
            if (file.isDirectory || !file.isFile)
                throwExceptionDataStoreCorrupted(fileContent.key)
            file.delete()
        }

        file.parentFile.mkdirs()

        val outputStream = FileOutputStream(file)
        IOUtils.copyLarge(fileContent.data, outputStream)
        IOUtils.closeQuietly(outputStream)
    }

    override fun find(key: FileKey): FileContent? {
        if (key.value.length < 4) throw IllegalArgumentException("Malformed file key")

        val file = getFileFromKey(key)

        if (!file.exists()) return null
        if (file.isDirectory || !file.isFile)
            throwExceptionDataStoreCorrupted(key)

        return FileContent(key, FileInputStream(file))
    }

    override fun delete(key: FileKey) {
        val file = getFileFromKey(key)
        if (!file.exists()) return

        if (file.isDirectory || !file.isFile)
            throwExceptionDataStoreCorrupted(key)

        if (!file.delete()) throw IllegalStateException("Unable to delete file $key")
        deleteDirIfEmpty(file.parentFile)
        deleteDirIfEmpty(file.parentFile.parentFile)
    }

    override fun exists(key: FileKey): Boolean {
        val file = getFileFromKey(key)
        if (file.isDirectory || !file.isFile)
            throwExceptionDataStoreCorrupted(key)

        return file.exists()
    }

    private fun getFileFromKey(key: FileKey): File = Paths.get(
        dataStorePath,
        key.value.substring(0, 2),
        key.value.substring(2, 4),
        key.value
    ).toFile()

    private fun throwExceptionDataStoreCorrupted(key: FileKey): Nothing =
        throw IllegalStateException("Requested file ${key.value} is not a regular file - datastore corrupted!")

    private fun deleteDirIfEmpty(file: File) {
        if (file.listFiles()?.isEmpty() ?: throw IllegalArgumentException("Not a directory!")) {
            file.delete()
        }
    }
}
