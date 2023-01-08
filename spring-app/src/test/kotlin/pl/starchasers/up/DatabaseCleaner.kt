package pl.starchasers.up

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import pl.starchasers.up.util.initializer.Initializer
import javax.sql.DataSource

@Component
class DatabaseCleaner(
    private val repositories: List<JpaRepository<*, *>>,
    private val initializers: List<Initializer>,
    private val dataSource: DataSource
) : BeforeTestExecutionCallback {

    fun reset() {
        clean()
        initialize()
    }

    private fun clean() {
        val connection = dataSource.connection

        connection.prepareStatement("SET session_replication_role = 'replica';").execute()
        repositories.forEach {
            it.deleteAllInBatch()
        }
        connection.prepareStatement("SET session_replication_role = 'origin';").execute()
        connection.close()
    }

    private fun initialize() {
        initializers.forEach { it.initialize() }
    }

    override fun beforeTestExecution(context: ExtensionContext?) {
        clean()
        initialize()
    }
}
