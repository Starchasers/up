package pl.starchasers.up.repository

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.io.File
import java.lang.IllegalStateException
import javax.annotation.PostConstruct

interface UploadRepository {

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


}