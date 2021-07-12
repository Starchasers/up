package pl.starchasers.up.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.*
import pl.starchasers.up.exception.AccessDeniedException
import pl.starchasers.up.exception.BadRequestException
import pl.starchasers.up.exception.InvalidCredentialsException
import pl.starchasers.up.exception.UserException
import pl.starchasers.up.repository.UserRepository
import pl.starchasers.up.security.Role
import pl.starchasers.up.util.encode
import pl.starchasers.up.util.matches
import java.security.Principal

const val ROOT_USER_NAME = "root"

interface UserService {
    fun getUser(id: Long): User

    fun getUser(username: Username): User

    fun findUser(id: Long): User?

    fun findUser(username: Username): User?

    fun fromPrincipal(principal: Principal?): User?

    fun createUser(username: Username, rawPassword: RawPassword, email: Email?, role: Role): User

    fun getUserFromCredentials(username: Username, password: RawPassword): User

    fun listUsers(pageable: Pageable): Page<User>

    fun updateUser(
        userId: Long,
        username: Username?,
        email: Email?,
        password: RawPassword?,
        role: Role?,
        maxTemporaryFileSize: FileSize?,
        maxPermanentFileSize: FileSize?,
        defaultFileLifetime: Milliseconds?,
        maxFileLifetime: Milliseconds?
    )

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

    override fun getUser(username: Username): User = userRepository.findFirstByUsername(username)
        ?: throw UserException("User with username '$username' doesn't exist")

    override fun findUser(id: Long): User? = userRepository.findFirstById(id)

    override fun findUser(username: Username): User? = userRepository.findFirstByUsername(username)

    override fun fromPrincipal(principal: Principal?): User? {
        if (principal == null) return null
        return findUser(principal.name.toLong())
    }

    override fun createUser(username: Username, rawPassword: RawPassword, email: Email?, role: Role): User {
        val oldUser = findUser(username)
        if (oldUser != null) throw BadRequestException("Username already taken.")
        val user = User(
            0,
            username,
            passwordEncoder.encode(rawPassword),
            email,
            role
        )
        configurationService.applyDefaultConfiguration(user)
        userRepository.save(user)
        return user
    }

    override fun getUserFromCredentials(username: Username, password: RawPassword): User =
        findUser(username)?.takeIf { passwordEncoder.matches(password, it.password) }
            ?: throw InvalidCredentialsException("Incorrect username or password")

    override fun listUsers(pageable: Pageable): Page<User> = userRepository.findAll(pageable)

    override fun updateUser(
        userId: Long,
        username: Username?,
        email: Email?,
        password: RawPassword?,
        role: Role?,
        maxTemporaryFileSize: FileSize?,
        maxPermanentFileSize: FileSize?,
        defaultFileLifetime: Milliseconds?,
        maxFileLifetime: Milliseconds?
    ) {
        val user = findUser(userId) ?: throw BadRequestException("User does not exist.")

        if (username != null && user.username != username) {
            if (findUser(username) != null) throw BadRequestException("Username already taken")
            user.username = username
        }

        user.email = email
        password?.let { user.password = passwordEncoder.encode(it) }
        role?.let { user.role = it }
        maxTemporaryFileSize?.let { user.maxTemporaryFileSize = it }
        maxPermanentFileSize?.let { user.maxPermanentFileSize = it }
        defaultFileLifetime?.let { user.defaultFileLifetime = it }
        maxFileLifetime?.let { user.maxFileLifetime = it }

        userRepository.save(user)
    }

    override fun deleteUser(user: User) {
        userRepository.delete(user)
    }

    override fun deleteUser(userId: Long, thisUserId: Long) {
        if (userId == thisUserId) throw BadRequestException("Cannot delete current user.")
        if (findUser(userId)?.username?.value == ROOT_USER_NAME) throw AccessDeniedException()

        val toDelete = findUser(userId) ?: throw BadRequestException("User does not exist")
        userRepository.delete(toDelete)
    }
}
