package pl.starchasers.up

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UpApplication

fun main(args: Array<String>) {
	runApplication<UpApplication>(*args)
}
