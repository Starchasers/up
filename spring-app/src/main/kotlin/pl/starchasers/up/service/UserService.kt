package pl.starchasers.up.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.FileSize
import pl.starchasers.up.data.value.Milliseconds
import pl.starchasers.up.exception.AccessDeniedException
import pl.starchasers.up.exception.UserException
import pl.starchasers.up.repository.UserRepository
import pl.starchasers.up.security.Role
import java.security.Principal

interface UserService {
    fun getUser(id: Long): User

    fun getUser(username: String): User

    fun findUser(id: Long): User?

    fun findUser(username: String): User?

    fun fromPrincipal(principal: Principal?): User?

    fun createUser(username: String, password: String, email: String?, role: Role): User

    fun getUserFromCredentials(username: String, password: String): User

    fun deleteUser(user: User)
}

@Service
class UserServiceImpl(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val configurationService: ConfigurationService
) : UserService {
    override fun getUser(id: Long): User = userRepository.findFirstById(id)
            ?: throw UserException("User with ID `$id` doesn't exist")

    override fun getUser(username: String): User = userRepository.findFirstByUsername(username)
            ?: throw UserException("User with username '$username' doesn't exist")

    override fun findUser(id: Long): User? = userRepository.findFirstById(id)

    override fun findUser(username: String): User? = userRepository.findFirstByUsername(username)

    override fun fromPrincipal(principal: Principal?): User? {
        if (principal == null) return null
        return findUser(principal.name.toLong())
    }

    override fun createUser(username: String, password: String, email: String?, role: Role): User {
        val user = User(
                0,
                username,
                passwordEncoder.encode(password),
                email,
                role
        )
        configurationService.applyDefaultConfiguration(user)
        userRepository.save(user)
        return user
    }

    override fun getUserFromCredentials(username: String, password: String): User =
            findUser(username)?.takeIf { passwordEncoder.matches(password, it.password) }
                    ?: throw AccessDeniedException("Incorrect username or password")

    override fun deleteUser(user: User) {
        userRepository.delete(user)
    }

}