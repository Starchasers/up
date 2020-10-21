package pl.starchasers.up.controller.admin

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.starchasers.up.data.dto.users.CreateUserDTO
import pl.starchasers.up.data.dto.users.UpdateUserDTO
import pl.starchasers.up.data.dto.users.UserDTO
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.Email
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.Username
import pl.starchasers.up.exception.AccessDeniedException
import pl.starchasers.up.exception.NotFoundException
import pl.starchasers.up.security.IsAdmin
import pl.starchasers.up.service.UserService
import java.security.Principal

@RestController
@RequestMapping("/api/admin/users")
class UserAdminController(
        private val userService: UserService
) {

    @IsAdmin
    @GetMapping("/{userId}")
    fun getOne(@PathVariable userId: Long): UserDTO {
        return userService.findUser(userId)?.toUserDTO()
                ?: throw NotFoundException()
    }


    @IsAdmin
    @GetMapping("")
    fun list(pageable: Pageable): Page<UserDTO> {
        return userService.listUsers(pageable).map { it.toUserDTO() }
    }

    @IsAdmin
    @PostMapping("")
    fun create(@Validated @RequestBody createUserDTO: CreateUserDTO): UserDTO {
        return userService.createUser(
                Username(createUserDTO.username),
                RawPassword(createUserDTO.password),
                if (!createUserDTO.email.isNullOrBlank()) Email(createUserDTO.email) else null,
                createUserDTO.role).toUserDTO()
    }

    @IsAdmin
    @PutMapping("/{userId}")
    fun update(@PathVariable userId: Long, @RequestBody userDTO: UpdateUserDTO) {
        userService.updateUser(
                userId,
                Username(userDTO.username),
                if (userDTO.email.isNullOrBlank()) null else Email(userDTO.email),
                if (userDTO.password.isNullOrBlank()) null else RawPassword(userDTO.password),
                userDTO.role)
    }

    @IsAdmin
    @DeleteMapping("/{userId}")
    fun delete(@PathVariable userId: Long, principal: Principal) {
        userService.deleteUser(userId, principal.name.toLongOrNull() ?: throw AccessDeniedException())
    }

    private fun User.toUserDTO() = UserDTO(id, username.value, email?.value ?: "", role)
}