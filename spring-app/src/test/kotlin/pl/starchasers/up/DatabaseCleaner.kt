package pl.starchasers.up

import org.junit.rules.ExternalResource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import pl.starchasers.up.util.initializer.Initializer
import javax.sql.DataSource

@Component
class DatabaseCleaner(
    private val repositories: List<JpaRepository<*, *>>,
    private val initializers: List<Initializer>,
    private val dataSource: DataSource
) : ExternalResource() {

    override fun before() {
        clean()
        initialize()
    }

    fun reset() {
        clean()
        initialize()
    }

    private fun clean() {
        val connection = dataSource.connection

        connection.prepareStatement("set foreign_key_checks = 0").execute()
        repositories.forEach {
            it.deleteAllInBatch()
        }
        connection.prepareStatement("set foreign_key_checks = 1").execute()
        connection.close()
    }

    private fun initialize() {
        initializers.forEach { it.initialize() }
    }
}