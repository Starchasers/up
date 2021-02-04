package pl.starchasers.up.configuration

import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import javax.sql.DataSource

@Configuration
@Profile("test", "localdb")
@PropertySource("classpath:application-localdb.properties")
class EmbeddedMariadbConfiguration {

    @Bean
    fun mariaDB4jSpringService() = MariaDB4jSpringService()

    @Bean
    fun dataSource(
        mariaDB4jSpringService: MariaDB4jSpringService,
        @Value("\${app.mariaDB4j.databaseName}") databaseName: String,
        @Value("\${spring.datasource.driver-class-name}") datasourceDriver: String
    ): DataSource {
        mariaDB4jSpringService.db.createDB(databaseName)

        val config = mariaDB4jSpringService.configuration

        return DataSourceBuilder
            .create()
            .url(config.getURL(databaseName))
            .driverClassName(datasourceDriver)
            .build()
    }
}
