package pl.starchasers.up.service

import org.springframework.stereotype.Service
import java.io.InputStream

interface FileStorageService {
    fun storeTemporaryFile(inputStream: InputStream);
}

@Service
class FileStorageServiceImpl() : FileStorageService {


    override fun storeTemporaryFile(inputStream: InputStream) {
        TODO()
    }


}