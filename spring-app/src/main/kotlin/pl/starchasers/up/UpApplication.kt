package pl.starchasers.up

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@ConfigurationPropertiesScan
@EnableMethodSecurity(
    securedEnabled = true,
    prePostEnabled = true
)
class UpApplication

fun main(args: Array<String>) {
    runApplication<UpApplication>(*args)
}
