package pl.starchasers.up.testdata

import org.springframework.stereotype.Service
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.Email
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.toUsername
import pl.starchasers.up.security.Role
import pl.starchasers.up.service.UserService

@Service
class UserTestData(
    private val userService: UserService
) {

    companion object {
        const val DEFAULT_USERNAME = "exampleUser"
        const val DEFAULT_PASSWORD = "password"
    }

    fun createTestUser(role: Role = Role.USER): User =
        userService.createUser(
            DEFAULT_USERNAME.toUsername(),
            RawPassword(DEFAULT_PASSWORD),
            Email("email@example.com"),
            role
        )
}
