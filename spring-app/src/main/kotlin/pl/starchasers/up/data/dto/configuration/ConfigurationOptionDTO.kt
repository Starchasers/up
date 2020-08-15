package pl.starchasers.up.data.dto.configuration

import pl.starchasers.up.data.model.ConfigurationKey

data class ConfigurationOptionDTO(
        val key: ConfigurationKey,

        /**
         * Value associated with given key
         */
        val value: String
)