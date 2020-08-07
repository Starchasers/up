package pl.starchasers.up.service

import org.springframework.stereotype.Service
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.FileSize
import pl.starchasers.up.data.value.Miliseconds

interface ConfigurationService {
    fun getMaxTemporaryFileSize(user: User?): FileSize

    fun getMaxPermanentFileSize(user: User?): FileSize

    fun getDefaultFileLifetime(user: User?): Miliseconds

    fun getMaxFileLifetime(user: User?): Miliseconds

    fun isPermanentAllowed(user: User?): Boolean
}

@Service
class ConfigurationServiceImpl() : ConfigurationService {


    override fun getMaxTemporaryFileSize(user: User?): FileSize {
        TODO("Not yet implemented")
    }

    override fun getMaxPermanentFileSize(user: User?): FileSize {
        TODO("Not yet implemented")
    }

    override fun getDefaultFileLifetime(user: User?): Miliseconds {
        TODO("Not yet implemented")
    }

    override fun getMaxFileLifetime(user: User?): Miliseconds {
        TODO("Not yet implemented")
    }

    override fun isPermanentAllowed(user: User?): Boolean {
        TODO("Not yet implemented")
    }

}