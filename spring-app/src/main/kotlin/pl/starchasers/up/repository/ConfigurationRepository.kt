package pl.starchasers.up.repository

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.insertOrUpdate
import org.springframework.stereotype.Service
import pl.starchasers.up.data.model.ConfigurationEntries
import pl.starchasers.up.data.model.ConfigurationEntry
import pl.starchasers.up.data.model.ConfigurationKey

@Service
class ConfigurationRepository(
    database: Database
) : StandardRepository<ConfigurationEntries>(ConfigurationEntries, database) {

    fun findFirstByKey(key: ConfigurationKey): ConfigurationEntry? {
        return database
            .from(table)
            .select()
            .where(table.key eq key)
            .limit(1)
            .map { row ->
                ConfigurationEntry(
                    id = row[table.id]!!,
                    key = row[table.key]!!,
                    value = row[table.value]!!
                )
            }
            .firstOrNull()
    }

    fun upsertValue(key: ConfigurationKey, value: String) {
        database.insertOrUpdate(table) {
            set(it.key, key)
            set(it.value, value)
            onConflict(it.key) {
                set(it.value, value)
            }
        }
    }
}

