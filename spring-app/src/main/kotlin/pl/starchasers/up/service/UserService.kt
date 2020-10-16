package pl.starchasers.up.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.FileSize
import pl.starchasers.up.data.value.Milliseconds
import pl.starchasers.up.exception.AccessDeniedException
import pl.starchasers.up.exception.BadRequestException
import pl.starchasers.up.exception.UserException
import pl.starchasers.up.repository.UserRepository
import pl.starchasers.up.security.Role
import java.security.Principal
import javax.persistence.Access

const val ROOT_USER_NAME = "root"

interface UserService {
    fun getUser(id: Long): User

    fun getUser(username: String): User

    fun findUser(id: Long): User?

    fun findUser(username: String): User?

    fun fromPrincipal(principal: Principal?): User?

    fun createUser(username: String, password: String, email: String?, role: Role): User

    fun getUserFromCredentials(username: String, password: String): User

    fun listUsers(pageable: Pageable): Page<User>

    fun updateUser(userId: Long, username: String, email: String?, password: String?, role: Role)

    fun deleteUser(user: User)

    fun deleteUser(userId: Long, thisUserId: Long)
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
        val oldUser = findUser(username)
        if (oldUser != null) throw BadRequestException()
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

    override fun listUsers(pageable: Pageable): Page<User> = userRepository.findAll(pageable)

    override fun updateUser(userId: Long, username: String, email: String?, password: String?, role: Role) {
        val user = findUser(userId) ?: throw BadRequestException()

        if (user.username != username) {
            if (findUser(username) != null) throw BadRequestException()
            user.username = username
        }

        user.email = email
        if (password != null) user.password = passwordEncoder.encode(password)
        user.role = role

        userRepository.save(user)
    }

    override fun deleteUser(user: User) {
        userRepository.delete(user)
    }

    override fun deleteUser(userId: Long, thisUserId: Long) {
        if (userId == thisUserId) throw BadRequestException()
        if (findUser(userId)?.username == ROOT_USER_NAME) throw AccessDeniedException()

        val toDelete = findUser(userId) ?: throw BadRequestException()
        userRepository.delete(toDelete)
    }

}