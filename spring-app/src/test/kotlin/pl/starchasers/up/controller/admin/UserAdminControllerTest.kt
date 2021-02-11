package pl.starchasers.up.controller.admin

import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
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

        private fun getOnePath(id: Long) = "/api/admin/users/$id"

        @DocumentResponse
        @Test
        fun `Given valid request, should return user details`() {
            mockMvc.get(getOnePath(user.id)) {
                cookie(getAdminAccessTokenCookie())
            }.andExpect {
                status { isOk() }
                content {
                    responsePath("$.id", Matchers.equalTo(user.id))
                    responsePath("$.username", Matchers.equalTo(user.username.value))
                    responsePath("$.email", Matchers.equalTo(user.email?.value))
                    responsePath("$.role", Matchers.equalTo(user.role.toString()))
                    responsePath("$.maxTemporaryFileSize", Matchers.equalTo(user.maxTemporaryFileSize.value))
                    responsePath("$.maxPermanentFileSize", Matchers.equalTo(user.maxPermanentFileSize.value))
                    responsePath("$.defaultFileLifetime", Matchers.equalTo(user.defaultFileLifetime.value))
                    responsePath("$.maxFileLifetime", Matchers.equalTo(user.maxFileLifetime.value))
                }
            }
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            mockMvc.get(getOnePath(user.id)).andExpect {
                status { isForbidden() }
            }
        }

        @Test
        fun `Given invalid id, should throw 404`() {
            mockMvc.get(getOnePath(user.id + 1024)) {
                cookie(getAdminAccessTokenCookie())
            }.andExpect {
                status { isNotFound() }
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

        private val onePath = "/api/admin/users"

        @DocumentResponse
        @Test
        fun `Given valid request, should return page`() {
            mockMvc.get(onePath) {
                cookie(getAdminAccessTokenCookie())
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            mockMvc.get(onePath).andExpect {
                status { isForbidden() }
            }
        }
    }

    @OrderTests
    @Nested
    inner class UsersCreate : MockMvcTestBase() {

        private val path = "/api/admin/users"

        @Autowired
        private lateinit var userService: UserService

        @DocumentResponse
        @Test
        fun `Given valid request, should create user`() {
            mockMvc.post(path) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
                content(CreateUserDTO("createdUser", "password", "mail@example.com", Role.USER))
            }.andExpect {
                status { is2xxSuccessful() }
                userService.getUser(Username("createdUser")).let {
                    content {
                        responsePath("$.id", Matchers.equalTo(it.id))
                        responsePath("$.username", Matchers.equalTo(it.username.value))
                        responsePath("$.email", Matchers.equalTo(it.email?.value))
                        responsePath("$.role", Matchers.equalTo(it.role.toString()))
                    }
                }
            }
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            mockMvc.post(path) {
                headers { contentTypeJson() }
                content(CreateUserDTO("createdUser", "password", "mail@example.com", Role.USER))
            }.andExpect {
                status { isForbidden() }
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

            mockMvc.post(path) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
                content(CreateUserDTO("duplicateUser", "password", "mail2@example.com", Role.ADMIN))
            }.andExpect {
                status { isBadRequest() }
            }
        }
    }

    @OrderTests
    @Nested
    inner class UsersUpdate : MockMvcTestBase() {

        private fun getUpdateUserPath(id: Long) = "/api/admin/users/$id"

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
            mockMvc.patch(getUpdateUserPath(oldUser.id)) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
                content(
                    UpdateUserDTO(
                        "newExampleUser",
                        "mail2@example.com",
                        "password2",
                        Role.ADMIN,
                        ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue.toLong(),
                        ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue.toLong(),
                        ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue.toLong(),
                        ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue.toLong()
                    )
                )
            }.andExpect {
                status { is2xxSuccessful() }
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
            mockMvc.patch(getUpdateUserPath(oldUser.id)) {
                headers { contentTypeJson() }
                content(
                    UpdateUserDTO(
                        "newExampleUser",
                        "mail2@example.com",
                        "password2",
                        Role.ADMIN,
                        ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue.toLong(),
                        ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue.toLong(),
                        ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue.toLong(),
                        ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue.toLong()
                    )
                )
            }.andExpect {
                status { isForbidden() }
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
            mockMvc.patch(getUpdateUserPath(oldUser.id)) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
                content(
                    UpdateUserDTO(
                        "newExampleUser",
                        "mail2@example.com",
                        null,
                        Role.ADMIN,
                        ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue.toLong(),
                        ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue.toLong(),
                        ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue.toLong(),
                        ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue.toLong()
                    )
                )
            }.andExpect {
                status { isOk() }
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
            mockMvc.patch(getUpdateUserPath(oldUser.id + 1024)) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
                content(
                    UpdateUserDTO(
                        "newExampleUser",
                        "mail2@example.com",
                        null,
                        Role.ADMIN,
                        ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue.toLong(),
                        ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue.toLong(),
                        ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue.toLong(),
                        ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue.toLong()
                    )
                )
            }.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `Given extra fields, should ignore them and process request`() {
            val oldUser = userService.createUser(
                Username("exampleUser"),
                RawPassword("password"),
                Email("email@example.com"),
                Role.USER
            )
            mockMvc.patch(getUpdateUserPath(oldUser.id)) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
                content(object {
                    val id = oldUser.id
                    val username = "newExampleUser"
                    val email = ""
                    val role = Role.USER
                })
            }.andExpect {
                status { isOk() }
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

        private fun getDeleteUserPath(id: Long) = "/api/admin/users/$id"

        @Test
        @DocumentResponse
        fun `Given valid request, should delete user`() {
            val user = userService.createUser(
                Username("userToDelete"),
                RawPassword("password"),
                Email("mail@example.com"),
                Role.USER
            )

            mockMvc.delete(getDeleteUserPath(user.id)) {
                cookie(getAdminAccessTokenCookie())
            }.andExpect {
                status { isOk() }
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

            mockMvc.delete(getDeleteUserPath(user.id)).andExpect {
                status { isForbidden() }
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

            mockMvc.delete(getDeleteUserPath(user.id + 1024)) {
                cookie(getAdminAccessTokenCookie())
            }.andExpect {
                status { isBadRequest() }
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

            mockMvc.delete(getDeleteUserPath(userService.getUser(Username("root")).id)) {
                cookie(createAccessTokenCookie(accessToken))
            }.andExpect {
                status { isForbidden() }
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

            mockMvc.delete(getDeleteUserPath(user.id)) {
                cookie(createAccessTokenCookie(accessToken))
            }.andExpect {
                status { isBadRequest() }
            }
        }
    }
}
