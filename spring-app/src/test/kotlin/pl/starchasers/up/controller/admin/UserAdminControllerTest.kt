package pl.starchasers.up.controller.admin

import no.skatteetaten.aurora.mockmvc.extensions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.users.CreateUserDTO
import pl.starchasers.up.data.dto.users.UpdateUserDTO
import pl.starchasers.up.data.model.ConfigurationKey
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.Email
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.security.Role
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.UserService


internal class UserAdminControllerTest : JpaTestBase() {

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @OrderTests
    @Nested
    inner class UsersGetOne : MockMvcTestBase() {
        @Autowired
        private lateinit var userService: UserService
        private lateinit var user: User

        @BeforeEach
        private fun createUsers() {
            user = userService.createUser(
                Username("username"),
                RawPassword("password"),
                Email("email@gmail.com"),
                Role.ADMIN
            )
            userService.createUser(
                Username("username2"),
                RawPassword("password2"),
                Email("email2@gmail.com"),
                Role.USER
            )
        }

        private fun getOnePath(id: Long): Path = Path("/api/admin/users/$id")

        @DocumentResponse
        @Test
        fun `Given valid request, should return user details`() {
            mockMvc.get(
                path = getOnePath(user.id),
                headers = HttpHeaders().authorization(getAdminAccessToken())
            ) {
                isSuccess()

                responseJsonPath("$.id").equalsValue(user.id)
                responseJsonPath("$.username").equalsValue(user.username.value)
                responseJsonPath("$.email").equalsValue(user.email?.value)
                responseJsonPath("$.role").equalsValue(user.role.toString())
                responseJsonPath("$.maxTemporaryFileSize").equalsValue(user.maxTemporaryFileSize.value)
                responseJsonPath("$.maxPermanentFileSize").equalsValue(user.maxPermanentFileSize.value)
                responseJsonPath("$.defaultFileLifetime").equalsValue(user.defaultFileLifetime.value)
                responseJsonPath("$.maxFileLifetime").equalsValue(user.maxFileLifetime.value)
            }
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            mockMvc.get(path = getOnePath(user.id)) {
                isError(HttpStatus.FORBIDDEN)
            }
        }

        @Test
        fun `Given invalid id, should throw 404`() {
            mockMvc.get(
                path = getOnePath(user.id + 123),
                headers = HttpHeaders().authorization(getAdminAccessToken())
            ) {
                isError(HttpStatus.NOT_FOUND)
            }
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @OrderTests
    @Nested
    inner class UsersList : MockMvcTestBase() {
        @Autowired
        private lateinit var userService: UserService
        private lateinit var user: User

        @BeforeEach
        private fun createUsers() {
            user = userService.createUser(
                Username("username3"),
                RawPassword("password"),
                Email("email@gmail.com"),
                Role.ADMIN
            )
            userService.createUser(
                Username("username4"),
                RawPassword("password2"),
                Email("email2@gmail.com"),
                Role.USER
            )
        }

        private val getOnePath: Path = Path("/api/admin/users")

        @DocumentResponse
        @Test
        fun `Given valid request, should return page`() {
            mockMvc.get(
                path = getOnePath,
                headers = HttpHeaders().authorization(getAdminAccessToken())
            ) {
                isSuccess()
            }
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            mockMvc.get(path = getOnePath) {
                isError(HttpStatus.FORBIDDEN)
            }
        }


    }

    @OrderTests
    @Nested
    inner class UsersCreate : MockMvcTestBase() {

        private val path = Path("/api/admin/users")

        @Autowired
        private lateinit var userService: UserService

        @DocumentResponse
        @Test
        fun `Given valid request, should create user`() {
            mockMvc.post(
                path = path,
                headers = HttpHeaders().authorization(getAdminAccessToken()).contentTypeJson(),
                body = CreateUserDTO("createdUser", "password", "mail@example.com", Role.USER)
            ) {
                isSuccess()
                userService.getUser(Username("createdUser")).let {
                    responseJsonPath("$.id").equalsValue(it.id)
                    responseJsonPath("$.username").equalsValue(it.username.value)
                    responseJsonPath("$.email").equalsValue(it.email?.value)
                    responseJsonPath("$.role").equalsValue(it.role.toString())
                }
            }
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            mockMvc.post(
                path = path,
                headers = HttpHeaders().contentTypeJson(),
                body = CreateUserDTO("createdUser", "password", "mail@example.com", Role.USER)
            ) {
                isError(HttpStatus.FORBIDDEN)
            }
        }

        @Test
        fun `Given duplicate username, should throw 400`() {
            userService.createUser(
                Username("duplicateUser"),
                RawPassword("password"),
                Email("mail@example.com"),
                Role.USER
            )
            mockMvc.post(
                path = path,
                headers = HttpHeaders().authorization(getAdminAccessToken()).contentTypeJson(),
                body = CreateUserDTO("duplicateUser", "password", "mail2@example.com", Role.ADMIN)
            ) {
                isError(HttpStatus.BAD_REQUEST)
            }
        }

    }

    @OrderTests
    @Nested
    inner class UsersUpdate : MockMvcTestBase() {

        private fun getUpdateUserPath(id: Long): Path = Path("/api/admin/users/$id")

        @Autowired
        private lateinit var userService: UserService

        @Autowired
        private lateinit var passwordEncoder: Pbkdf2PasswordEncoder

        @DocumentResponse
        @Test
        fun `Given valid request, should update user`() {
            val oldUser = userService.createUser(
                Username("exampleUser"),
                RawPassword("password"),
                Email("email@example.com"),
                Role.USER
            )
            mockMvc.patch(
                path = getUpdateUserPath(oldUser.id),
                headers = HttpHeaders().authorization(getAdminAccessToken()).contentTypeJson(),
                body = UpdateUserDTO(
                    "newExampleUser",
                    "mail2@example.com",
                    "password2",
                    Role.ADMIN,
                    ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue.toLong(),
                    ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue.toLong(),
                    ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue.toLong(),
                    ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue.toLong()
                )
            ) {
                isSuccess()
            }
            userService.getUser(oldUser.id).let {
                assertEquals("mail2@example.com", it.email?.value)
                assertEquals("newExampleUser", it.username.value)
                assertEquals(Role.ADMIN, it.role)
                assertTrue(passwordEncoder.matches("password2", it.password.value))
            }
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            val oldUser = userService.createUser(
                Username("exampleUser"),
                RawPassword("password"),
                Email("email@example.com"),
                Role.USER
            )
            mockMvc.patch(
                path = getUpdateUserPath(oldUser.id),
                headers = HttpHeaders().contentTypeJson(),
                body = UpdateUserDTO(
                    "newExampleUser",
                    "mail2@example.com",
                    "password2",
                    Role.ADMIN,
                    ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue.toLong(),
                    ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue.toLong(),
                    ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue.toLong(),
                    ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue.toLong()
                )
            ) {
                isError(HttpStatus.FORBIDDEN)
            }
        }

        @Test
        fun `Given no password field, should not update password`() {
            val oldUser = userService.createUser(
                Username("exampleUser"),
                RawPassword("password"),
                Email("email@example.com"),
                Role.USER
            )
            mockMvc.patch(
                path = getUpdateUserPath(oldUser.id),
                headers = HttpHeaders().authorization(getAdminAccessToken()).contentTypeJson(),
                body = UpdateUserDTO(
                    "newExampleUser",
                    "mail2@example.com",
                    null,
                    Role.ADMIN,
                    ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue.toLong(),
                    ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue.toLong(),
                    ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue.toLong(),
                    ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue.toLong()
                )
            ) {
                isSuccess()
            }
            assertTrue(passwordEncoder.matches("password", userService.getUser(oldUser.id).password.value))
        }

        @Test
        fun `Given wrong userId, should return 400`() {
            val oldUser = userService.createUser(
                Username("exampleUser"),
                RawPassword("password"),
                Email("email@example.com"),
                Role.USER
            )
            mockMvc.patch(
                path = getUpdateUserPath(oldUser.id + 123),
                headers = HttpHeaders().authorization(getAdminAccessToken()).contentTypeJson(),
                body = UpdateUserDTO(
                    "newExampleUser",
                    "mail2@example.com",
                    null,
                    Role.ADMIN,
                    ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue.toLong(),
                    ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue.toLong(),
                    ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue.toLong(),
                    ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue.toLong()
                )
            ) {
                isError(HttpStatus.BAD_REQUEST)
            }
        }

        @Test
        fun `Given extra fields, should ignore them and process request`() {
            val oldUser = userService.createUser(
                Username("exampleUser"),
                RawPassword("password"),
                Email("email@example.com"), Role.USER
            )
            mockMvc.patch(path = getUpdateUserPath(oldUser.id),
                headers = HttpHeaders().authorization(getAdminAccessToken()).contentTypeJson(),
                body = object {
                    val id = oldUser.id
                    val username = "newExampleUser"
                    val email = ""
                    val role = Role.USER
                }) {

                isSuccess()
            }

            userService.getUser(oldUser.id).let {
                assertEquals(null, it.email)
                assertEquals("newExampleUser", it.username.value)
                assertEquals(Role.USER, it.role)
                assertTrue(passwordEncoder.matches("password", it.password.value))
            }
        }

    }

    @OrderTests
    @Nested
    inner class UsersDelete : MockMvcTestBase() {

        @Autowired
        private lateinit var userService: UserService

        @Autowired
        private lateinit var jwtTokenService: JwtTokenService

        private fun getDeleteUserPath(id: Long) = Path("/api/admin/users/$id")

        @Test
        @DocumentResponse
        fun `Given valid request, should delete user`() {
            val user = userService.createUser(
                Username("userToDelete"),
                RawPassword("password"),
                Email("mail@example.com"), Role.USER
            )

            mockMvc.delete(
                path = getDeleteUserPath(user.id),
                headers = HttpHeaders().authorization(getAdminAccessToken())
            ) {
                isSuccess()
            }
            assertNull(userService.findUser(user.id))
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            val user = userService.createUser(
                Username("userToDelete"),
                RawPassword("password"),
                Email("mail@example.com"),
                Role.USER
            )

            mockMvc.delete(path = getDeleteUserPath(user.id)) {
                isError(HttpStatus.FORBIDDEN)
            }
            assertNotNull(userService.findUser(user.id))
        }

        @Test
        fun `Given wrong userId, should return 400`() {
            val user = userService.createUser(
                Username("userToDelete"),
                RawPassword("password"),
                Email("mail@example.com"),
                Role.USER
            )

            mockMvc.delete(
                path = getDeleteUserPath(user.id + 123),
                headers = HttpHeaders().authorization(getAdminAccessToken())
            ) {
                isError(HttpStatus.BAD_REQUEST)
            }
        }

        @Test
        fun `Given root account, should return 403`() {
            val user = userService.createUser(
                Username("userToDelete"),
                RawPassword("password"),
                Email("mail@example.com"),
                Role.ADMIN
            )
            val accessToken = jwtTokenService.issueAccessToken(jwtTokenService.issueRefreshToken(user))

            mockMvc.delete(
                path = getDeleteUserPath(userService.getUser(Username("root")).id),
                headers = HttpHeaders().authorization(accessToken)
            ) {
                isError(HttpStatus.FORBIDDEN)
            }
        }

        @Test
        fun `Given current account, should return 400`() {
            val user = userService.createUser(
                Username("userToDelete"),
                RawPassword("password"),
                Email("mail@example.com"),
                Role.ADMIN
            )
            val accessToken = jwtTokenService.issueAccessToken(jwtTokenService.issueRefreshToken(user))

            mockMvc.delete(
                path = getDeleteUserPath(user.id),
                headers = HttpHeaders().authorization(accessToken)
            ) {
                isError(HttpStatus.BAD_REQUEST)
            }
        }
    }


}