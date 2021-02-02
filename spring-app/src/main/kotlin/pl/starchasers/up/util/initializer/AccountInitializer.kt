package pl.starchasers.up.util.initializer

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.security.Role
import pl.starchasers.up.service.UserService
import pl.starchasers.up.util.Util
import javax.annotation.PostConstruct

@Component
class AccountInitializer(
    private val userService: UserService
) : Initializer() {

    private val util = Util()
    private val logger = LoggerFactory.getLogger(AccountInitializer::class.java)

    @PostConstruct
    override fun initialize() {
        ensureRootAccount()
    }

    private fun ensureRootAccount() {
        val user = userService.findUser(Username("root"))

        if (user == null) {
            val password = util.secureAlphanumericRandomString(16)
            userService.createUser(Username("root"), RawPassword(password), null, Role.ADMIN)
            logger.info("Root account not found. Creating new one.\n\nUsername: root Password: $password\n")
        }
    }
}