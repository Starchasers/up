package pl.starchasers.up.data.dto.configuration

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import pl.starchasers.up.data.model.ConfigurationKey

@JsonIgnoreProperties(ignoreUnknown = true)
data class ConfigurationDTO(
        val options: Map<ConfigurationKey, String>
)