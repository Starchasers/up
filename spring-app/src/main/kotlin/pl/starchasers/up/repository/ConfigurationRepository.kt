package pl.starchasers.up.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.starchasers.up.data.model.ConfigurationEntry
import pl.starchasers.up.data.model.ConfigurationKey

@Repository
interface ConfigurationRepository : JpaRepository<ConfigurationEntry, Long> {

    fun findFirstByKey(key: ConfigurationKey): ConfigurationEntry?
}
