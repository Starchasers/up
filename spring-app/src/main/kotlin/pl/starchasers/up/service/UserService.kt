package pl.starchasers.up.service

import org.springframework.stereotype.Service
import pl.starchasers.up.data.model.User
import pl.starchasers.up.exception.UserException
import pl.starchasers.up.repository.UserRepository

interface UserService {
    fun getUser(id: Long): User

    fun getUser(username: String): User

    fun findUser(id: Long): User?

    fun findUser(username: String): User?
}

@Service
class UserServiceImpl(
        private val userRepository: UserRepository
) : UserService {
    override fun getUser(id: Long): User = userRepository.findFirstById(id)
            ?: throw UserException("User with ID `$id` doesn't exist")

    override fun getUser(username: String): User = userRepository.findFirstByUsername(username)
            ?: throw UserException("User with username '$username' doesn't exist")

    override fun findUser(id: Long): User? = userRepository.findFirstById(id)

    override fun findUser(username: String): User? = userRepository.findFirstByUsername(username)

}