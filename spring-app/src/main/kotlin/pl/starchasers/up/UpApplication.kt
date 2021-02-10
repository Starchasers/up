package pl.starchasers.up

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication()
@EnableScheduling
@EnableTransactionManagement
class UpApplication

fun main(args: Array<String>) {
    runApplication<UpApplication>(*args)
}
