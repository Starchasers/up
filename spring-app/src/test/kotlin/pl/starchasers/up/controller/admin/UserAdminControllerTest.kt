package pl.starchasers.up.controller.admin

import assertk.assertAll
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.skatteetaten.aurora.mockmvc.extensions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.users.CreateUserDTO
import pl.starchasers.up.data.dto.users.UpdateUserDTO
import pl.starchasers.up.data.model.User
import pl.starchasers.up.security.Role
import pl.starchasers.up.service.UserService
import javax.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
internal class UserAdminControllerTest() : MockMvcTestBase() {

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Transactional
    @OrderTests
    @Nested
    inner class UsersGetOne : MockMvcTestBase() {
        @Autowired
        private lateinit var userService: UserService
        private lateinit var user: User

        @BeforeAll
        private fun createUsers() {
            user = userService.createUser("username", "password", "email@gmail.com", Role.ADMIN)
            userService.createUser("username2", "password2", "email2@gmail.com", Role.USER)
        }

        private fun getOnePath(id: Long): Path = Path("/api/admin/users/$id")

        @DocumentResponse
        @Test
        fun `Given valid request, should return user details`() {
            mockMvc.get(path = getOnePath(user.id),
                    headers = HttpHeaders().authorization(getAdminAccessToken())) {
                isSuccess()

                responseJsonPath("$.id").equalsValue(user.id)
                responseJsonPath("$.username").equalsValue(user.username)
                responseJsonPath("$.email").equalsValue(user.email)
                responseJsonPath("$.role").equalsValue(user.role.toString())
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
            mockMvc.get(path = getOnePath(user.id + 123),
                    headers = HttpHeaders().authorization(getAdminAccessToken())) {
                isError(HttpStatus.NOT_FOUND)
            }
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Transactional
    @OrderTests
    @Nested
    inner class UsersList : MockMvcTestBase() {
        @Autowired
        private lateinit var userService: UserService
        private lateinit var user: User

        @BeforeAll
        private fun createUsers() {
            user = userService.createUser("username3", "password", "email@gmail.com", Role.ADMIN)
            userService.createUser("username4", "password2", "email2@gmail.com", Role.USER)
        }

        private val getOnePath: Path = Path("/api/admin/users/")

        @DocumentResponse
        @Test
        fun `Given valid request, should return page`() {
            mockMvc.get(path = getOnePath,
                    headers = HttpHeaders().authorization(getAdminAccessToken())) {
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

    @Transactional
    @OrderTests
    @Nested
    inner class UsersCreate : MockMvcTestBase() {

        private val path = Path("/api/admin/users")

        @Autowired
        private lateinit var userService: UserService

        @DocumentResponse
        @Test
        fun `Given valid request, should create user`() {
            mockMvc.post(path = path,
                    headers = HttpHeaders().authorization(getAdminAccessToken()).contentTypeJson(),
                    body = CreateUserDTO("createdUser", "password", "mail@example.com", Role.USER)) {
                isSuccess()
                userService.getUser("createdUser").let {
                    responseJsonPath("$.id").equalsValue(it.id)
                    responseJsonPath("$.username").equalsValue(it.username)
                    responseJsonPath("$.email").equalsValue(it.email)
                    responseJsonPath("$.role").equalsValue(it.role.toString())
                }
            }
        }

        @Test
        fun `Given unauthorized user, should throw 403`() {
            mockMvc.post(path = path,
                    headers = HttpHeaders().contentTypeJson(),
                    body = CreateUserDTO("createdUser", "password", "mail@example.com", Role.USER)) {
                isError(HttpStatus.FORBIDDEN)
            }
        }

        @Test
        fun `Given duplicate username, should throw 400`() {
            userService.createUser("duplicateUser", "password", "mail@example.com", Role.USER)
            mockMvc.post(path = path,
                    headers = HttpHeaders().authorization(getAdminAccessToken()).contentTypeJson(),
                    body = CreateUserDTO("duplicateUser", "password", "mail2@example.com", Role.ADMIN)) {
                isError(HttpStatus.BAD_REQUEST)
            }
        }

    }

    @Transactional
    @OrderTests
    @Nested
    inner class UsersUpdate : MockMvcTestBase() {

        private fun getUpdateUserPath(id: Long): Path = Path("/api/admin/users/$id")

        @Autowired
        private lateinit var userService: UserService

        @Autowired
        private lateinit var passwordEncoder: Pbkdf2PasswordEncoder

        @Transactional
        @DocumentResponse
        @Test
        fun `Given valid request, should update user`() {
            val oldUser = userService.createUser("exampleUser", "password", "email@example.com", Role.USER)
            flush()
            mockMvc.put(path = getUpdateUserPath(oldUser.id),
                    headers = HttpHeaders().authorization(getAdminAccessToken()).contentTypeJson(),
                    body = UpdateUserDTO("mail2@example.com", "password2", Role.ADMIN)) {
                isSuccess()
            }
            flush()
            userService.getUser(oldUser.id).let {
                assertEquals("mail2@example.com", it.email)
                assertEquals("exampleUser", it.username)
                assertEquals(Role.ADMIN, it.role)
                assertTrue(passwordEncoder.matches("password2", it.password))
            }
        }

        @Transactional
        @Test
        fun `Given unauthorized user, should throw 403`() {
            val oldUser = userService.createUser("exampleUser", "password", "email@example.com", Role.USER)
            flush()
            mockMvc.put(path = getUpdateUserPath(oldUser.id),
                    headers = HttpHeaders().contentTypeJson(),
                    body = UpdateUserDTO("mail2@example.com", "password2", Role.ADMIN)) {
                isError(HttpStatus.FORBIDDEN)
            }
        }

        @Transactional
        @Test
        fun `Given no password field, should not update password`() {
            val oldUser = userService.createUser("exampleUser", "password", "email@example.com", Role.USER)
            flush()
            mockMvc.put(path = getUpdateUserPath(oldUser.id),
                    headers = HttpHeaders().authorization(getAdminAccessToken()).contentTypeJson(),
                    body = UpdateUserDTO("mail2@example.com", null, Role.ADMIN)) {
                isSuccess()
            }
            flush()
            assertTrue(passwordEncoder.matches("password", userService.getUser(oldUser.id).password))
        }

        @Transactional
        @Test
        fun `Given wrong userId, should return 400`() {
            val oldUser = userService.createUser("exampleUser", "password", "email@example.com", Role.USER)
            flush()
            mockMvc.put(path = getUpdateUserPath(oldUser.id + 123),
                    headers = HttpHeaders().authorization(getAdminAccessToken()).contentTypeJson(),
                    body = UpdateUserDTO("mail2@example.com", null, Role.ADMIN)) {
                isError(HttpStatus.BAD_REQUEST)
            }
        }

    }

    @Transactional
    @OrderTests
    @Nested
    inner class UsersDelete : MockMvcTestBase() {

//        @Test
//        @DocumentResponse
        fun `Given valid request, should delete user`() {

        }

//        @Test
        fun `Given unauthorized user, should throw 403`() {

        }

//        @Test
        fun `Given wrong userId, should return 400`() {

        }

//        @Test
        fun `Given root account, should return 403`() {

        }

//        @Test
        fun `Given current account, should return 400`() {

        }
    }


}