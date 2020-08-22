package pl.starchasers.up.data.dto.configuration

import pl.starchasers.up.data.model.ConfigurationKey

data class ConfigurationDTO(
        val options: Map<ConfigurationKey, String>
)