package pl.starchasers.up.controller.admin

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.users.CreateUserDTO
import pl.starchasers.up.data.dto.users.UpdateUserDTO
import pl.starchasers.up.data.dto.users.UserDTO
import pl.starchasers.up.data.model.ConfigurationKey
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.Email
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.data.value.toUsername
import pl.starchasers.up.security.Role
import pl.starchasers.up.service.UserService
import pl.starchasers.up.testdata.UserTestData

internal class UserAdminControllerTest : JpaTestBase() {

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class UsersGetOne : MockMvcTestBase() {
        @Autowired
        private lateinit var userService: UserService
        private lateinit var adminUser: User
        private lateinit var user: User

        @BeforeEach
        fun createUsers() {
            adminUser = userService.createUser(
                Username("username"),
                RawPassword("password"),
                Email("email@gmail.com"),
                Role.ADMIN
            )
            user = userService.createUser(
                Username("username2"),
                RawPassword("password2"),
                Email("email2@gmail.com"),
                Role.USER
            )
        }

        private val requestPath = "/api/admin/users/{id}"

        @Test
        fun `Given valid request, should return user details`() {
            val result: UserDTO = mockMvc.get(requestPath, adminUser.id) {
                authorizeAsAdmin()
            }.andExpect {
                status { isOk() }
            }.andReturn().parse()

            with(result) {
                id shouldBe adminUser.id
                username shouldBe adminUser.username.value
                email shouldBe adminUser.email?.value
                role shouldBe adminUser.role
                maxTemporaryFileSize shouldBe adminUser.maxTemporaryFileSize.value
                maxPermanentFileSize shouldBe adminUser.maxPermanentFileSize.value
                defaultFileLifetime shouldBe adminUser.defaultFileLifetime.value
                maxFileLifetime shouldBe adminUser.maxFileLifetime.value
            }
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            mockMvc.get(requestPath, adminUser.id) {
                authorizeAsUser(user)
            }.andExpect {
                status { isForbidden() }
            }
        }

        @Test
        fun `Given invalid id, should throw 404`() {
            mockMvc.get(requestPath, adminUser.id + 123) {
                authorizeAsAdmin()
            }.andExpect {
                status { isNotFound() }
            }
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class UsersList : MockMvcTestBase() {
        @Autowired
        private lateinit var userService: UserService
        private lateinit var adminUser: User
        private lateinit var user: User

        @BeforeEach
        fun createUsers() {
            adminUser = userService.createUser(
                Username("username3"),
                RawPassword("password"),
                Email("email@gmail.com"),
                Role.ADMIN
            )
            user = userService.createUser(
                Username("username4"),
                RawPassword("password2"),
                Email("email2@gmail.com"),
                Role.USER
            )
        }

        private val requestPath = "/api/admin/users"

        @Test
        fun `Given valid request, should return page`() {
            mockMvc.get(requestPath) {
                authorizeAsAdmin()
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            mockMvc.get(requestPath) {
                authorizeAsUser(user)
            }.andExpect {
                status { isForbidden() }
            }
        }
    }

    @Nested
    inner class UsersCreate : MockMvcTestBase() {

        private val requestPath = "/api/admin/users"

        @Autowired
        private lateinit var userService: UserService

        @Autowired
        private lateinit var userTestData: UserTestData

        @Test
        fun `Given valid request, should create user`() {
            val newUserUsername = "createdUser"

            val response: UserDTO = mockMvc.postJson(requestPath) {
                jsonContent = CreateUserDTO(newUserUsername, "password", "mail@example.com", Role.USER)
                authorizeAsAdmin()
            }.andExpect {
                status { isOk() }
            }.andReturn().parse()

            with(userService.getUser(newUserUsername.toUsername())) {
                response.id shouldBe id
                response.username shouldBe username.value
                response.email shouldBe email?.value
                response.role shouldBe role
            }
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            mockMvc.postJson(requestPath) {
                jsonContent = CreateUserDTO("createdUser", "password", "mail@example.com", Role.USER)
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
            mockMvc.postJson(requestPath) {
                jsonContent = CreateUserDTO("duplicateUser", "password", "mail2@example.com", Role.ADMIN)
                authorizeAsAdmin()
            }.andExpect {
                status { isBadRequest() }
            }
        }
    }

    @Nested
    inner class UsersUpdate : MockMvcTestBase() {

        private val requestPath = "/api/admin/users/{id}"

        @Autowired
        private lateinit var userService: UserService

        @Autowired
        private lateinit var userTestData: UserTestData

        @Autowired
        private lateinit var passwordEncoder: Pbkdf2PasswordEncoder

        @Test
        fun `Given valid request, should update user`() {
            val oldUser = userTestData.createTestUser()
            val updateUserDTO = getUpdateUserDTO()

            mockMvc.patchJson(requestPath, oldUser.id) {
                jsonContent = updateUserDTO
                authorizeAsAdmin()
            }.andExpect {
                status { isOk() }
            }

            with(userService.getUser(oldUser.id)) {
                email?.value shouldBe updateUserDTO.email
                username.value shouldBe updateUserDTO.username
                role shouldBe Role.ADMIN
                passwordEncoder.matches(updateUserDTO.password, password.value) shouldBe true
            }
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            val oldUser = userTestData.createTestUser()

            mockMvc.patchJson(requestPath, oldUser.id) {
                jsonContent = getUpdateUserDTO()
                authorizeAsUser(oldUser)
            }.andExpect {
                status { isForbidden() }
            }
        }

        @Test
        fun `Given no password field, should not update password`() {
            val oldUser = userTestData.createTestUser()
            mockMvc.patchJson(requestPath, oldUser.id) {
                jsonContent = getUpdateUserDTO(password = null)
                authorizeAsAdmin()
            }.andExpect {
                status { isOk() }
            }

            assertTrue(passwordEncoder.matches("password", userService.getUser(oldUser.id).password.value))
        }

        @Test
        fun `Given wrong userId, should return 400`() {
            val oldUser = userTestData.createTestUser()

            mockMvc.patchJson(requestPath, oldUser.id + 123) {
                jsonContent = getUpdateUserDTO()
                authorizeAsAdmin()
            }.andExpect {
                status { isBadRequest() }
            }
        }

        private fun getUpdateUserDTO(password: String? = "password2"): UpdateUserDTO = UpdateUserDTO(
            username = "newExampleUser",
            email = "mail2@example.com",
            password = password,
            role = Role.ADMIN,
            maxTemporaryFileSize = ConfigurationKey.DEFAULT_USER_MAX_TEMPORARY_FILE_SIZE.defaultValue.toLong(),
            maxPermanentFileSize = ConfigurationKey.DEFAULT_USER_MAX_PERMANENT_FILE_SIZE.defaultValue.toLong(),
            defaultFileLifetime = ConfigurationKey.DEFAULT_USER_DEFAULT_FILE_LIFETIME.defaultValue.toLong(),
            maxFileLifetime = ConfigurationKey.DEFAULT_USER_MAX_FILE_LIFETIME.defaultValue.toLong()
        )
    }

    @Nested
    inner class UsersDelete : MockMvcTestBase() {

        @Autowired
        private lateinit var userService: UserService

        @Autowired
        private lateinit var userTestData: UserTestData

        private val requestPath = "/api/admin/users/{id}"

        @Test
        fun `Given valid request, should delete user`() {
            val user = userTestData.createTestUser()

            mockMvc.delete(requestPath, user.id) {
                authorizeAsAdmin()
            }.andExpect {
                status { isOk() }
            }

            assertNull(userService.findUser(user.id))
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            val user = userTestData.createTestUser()

            mockMvc.delete(requestPath, user.id) {
                authorizeAsUser(user)
            }.andExpect {
                status { isForbidden() }
            }

            assertNotNull(userService.findUser(user.id))
        }

        @Test
        fun `Given wrong userId, should return 400`() {
            val user = userTestData.createTestUser()

            mockMvc.delete(requestPath, user.id + 123) {
                authorizeAsAdmin()
            }.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `Given root account, should return 403`() {
            val user = userTestData.createTestUser(role = Role.ADMIN)

            mockMvc.delete(requestPath, userService.getUser(Username("root")).id) {
                authorizeAsUser(user)
            }.andExpect {
                status { isForbidden() }
            }
        }

        @Test
        fun `Given current account, should return 400`() {
            val user = userTestData.createTestUser(role = Role.ADMIN)

            mockMvc.delete(requestPath, user.id) {
                authorizeAsUser(user)
            }.andExpect {
                status { isBadRequest() }
            }
        }
    }
}
