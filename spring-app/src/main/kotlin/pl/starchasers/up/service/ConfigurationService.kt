package pl.starchasers.up.service

import org.springframework.stereotype.Service
import pl.starchasers.up.data.model.ConfigurationKey
import pl.starchasers.up.data.model.User
import javax.annotation.PostConstruct

interface ConfigurationService {
    fun applyDefaultConfiguration(user: User)

    fun setConfigurationOption(key: ConfigurationKey, value: String)

    fun getConfigurationOption(key: ConfigurationKey): String
}

@Service
class ConfigurationServiceImpl() : ConfigurationService {

    override fun applyDefaultConfiguration(user: User) {
        TODO("Not yet implemented")
    }

    override fun setConfigurationOption(key: ConfigurationKey, value: String) {
        TODO("Not yet implemented")
    }

    override fun getConfigurationOption(key: ConfigurationKey): String {
        TODO("Not yet implemented")
    }

    @PostConstruct
    private fun initialize() {

    }

}