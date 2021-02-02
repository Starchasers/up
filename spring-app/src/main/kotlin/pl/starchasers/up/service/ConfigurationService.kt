package pl.starchasers.up.service

import org.springframework.stereotype.Service
import pl.starchasers.up.data.dto.configuration.UpdateUserConfigurationDTO
import pl.starchasers.up.data.model.ConfigurationEntry
import pl.starchasers.up.data.model.ConfigurationKey
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.FileSize
import pl.starchasers.up.data.value.Milliseconds
import pl.starchasers.up.exception.BadRequestException
import pl.starchasers.up.repository.ConfigurationRepository
import pl.starchasers.up.repository.UserRepository
import javax.annotation.PostConstruct
import javax.transaction.Transactional

interface ConfigurationService {
    fun applyDefaultConfiguration(user: User)

    fun updateUserConfiguration(user: User, configuration: UpdateUserConfigurationDTO)

    fun setConfigurationOption(key: ConfigurationKey, value: String)

    fun getConfigurationOption(key: ConfigurationKey): String

    fun getGlobalConfiguration(): Map<ConfigurationKey, String>

    fun updateGlobalConfiguration(configuration: Map<ConfigurationKey, String>)

    fun getAnonymousMaxFileSize(): FileSize

    fun getAnonymousDefaultFileLifetime(): Milliseconds

    fun getAnonymousMaxFileLifetime(): Milliseconds
}

@Service
class ConfigurationServiceImpl(
        private val configurationRepository: ConfigurationRepository,
        private val userRepository: UserRepository
) : ConfigurationService {

    override fun applyDefaultConfiguration(user: User) {
        user.apply {
            maxTemporaryFileSize = FileSize(getConfigurationOption(
                    ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE).toLong())
            maxPermanentFileSize = FileSize(getConfigurationOption(
                    ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE).toLong())
            defaultFileLifetime = Milliseconds(getConfigurationOption(
                    ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME).toLong())
            maxFileLifetime = Milliseconds(getConfigurationOption(
                    ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME).toLong())
        }
    }

    override fun updateUserConfiguration(user: User, configuration: UpdateUserConfigurationDTO) {
        user.apply {
            maxTemporaryFileSize = FileSize(configuration.maxTemporaryFileSize)
            maxPermanentFileSize = FileSize(configuration.maxPermanentFileSize)
            defaultFileLifetime = Milliseconds(configuration.defaultFileLifetime)
            maxFileLifetime = Milliseconds(configuration.maxFileLifetime)
        }
        userRepository.save(user)
    }

    override fun setConfigurationOption(key: ConfigurationKey, value: String) {
        if (value.toLongOrNull() == null) throw BadRequestException("Value must be of type Long.")//TODO change if more data types are required
        val entry = configurationRepository.findFirstByKey(key) ?: ConfigurationEntry(0, key, value)
        entry.value = value
        configurationRepository.save(entry)
    }

    override fun getConfigurationOption(key: ConfigurationKey): String {
        return configurationRepository.findFirstByKey(key)?.value ?: key.defaultValue
    }

    override fun getGlobalConfiguration(): Map<ConfigurationKey, String> =
            mapOf(*ConfigurationKey.values().map {
                Pair(it, configurationRepository.findFirstByKey(it)?.value ?: it.defaultValue)
            }.toTypedArray())

    override fun updateGlobalConfiguration(configuration: Map<ConfigurationKey, String>) {
        if (configuration.values.any { it.toLongOrNull() == null }) throw BadRequestException("Value must be of type Long.")//TODO change if more data types are required

        configuration.forEach { setConfigurationOption(it.key, it.value) }
    }

    override fun getAnonymousMaxFileSize(): FileSize =
            FileSize(getConfigurationOption(ConfigurationKey.ANONYMOUS_MAX_FILE_SIZE).toLong())

    override fun getAnonymousDefaultFileLifetime(): Milliseconds =
            Milliseconds(getConfigurationOption(ConfigurationKey.ANONYMOUS_DEFAULT_FILE_LIFETIME).toLong())

    override fun getAnonymousMaxFileLifetime(): Milliseconds =
            Milliseconds(getConfigurationOption(ConfigurationKey.ANONYMOUS_MAX_FILE_LIFETIME).toLong())

    @PostConstruct
    private fun initialize() {
        ConfigurationKey.values().forEach { key ->
            val entry = configurationRepository.findFirstByKey(key)

            if (entry == null) {
                val defaultEntry = ConfigurationEntry(0, key, key.defaultValue)
                configurationRepository.save(defaultEntry)
            }
        }
    }

}