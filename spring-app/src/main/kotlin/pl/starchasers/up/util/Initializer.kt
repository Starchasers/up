package pl.starchasers.up.util

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pl.starchasers.up.security.Role
import pl.starchasers.up.service.UserService
import javax.annotation.PostConstruct

@Component
class Initializer(
        private val userService: UserService
) {

    private val util = Util()
    private val logger = LoggerFactory.getLogger(Initializer::class.java)

    @PostConstruct
    fun setupApp() {
        ensureRootAccount()
    }

    private fun ensureRootAccount() {
        val user = userService.findUser("root")

        if (user == null) {
            val password = util.secureAlphanumericRandomString(16)
            userService.createUser("root", password, null, Role.ADMIN)
            logger.info("Root account not found. Creating new one.\n\nUsername: root Password: $password\n")
        }
    }
}