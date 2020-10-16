package pl.starchasers.up.data.dto.configuration

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import pl.starchasers.up.data.model.ConfigurationKey

@JsonIgnoreProperties(ignoreUnknown = true)
data class ConfigurationOptionDTO(
        val key: ConfigurationKey,

        /**
         * Value associated with given key
         */
        val value: String
)