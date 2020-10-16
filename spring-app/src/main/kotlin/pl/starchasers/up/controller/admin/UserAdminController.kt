package pl.starchasers.up.controller.admin

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import pl.starchasers.up.data.dto.users.CreateUserDTO
import pl.starchasers.up.data.dto.users.UpdateUserDTO
import pl.starchasers.up.data.dto.users.UserDTO
import pl.starchasers.up.data.model.User
import pl.starchasers.up.exception.AccessDeniedException
import pl.starchasers.up.exception.BadRequestException
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
        return userService.createUser(createUserDTO.username, createUserDTO.password, createUserDTO.email, createUserDTO.role).toUserDTO()
    }

    @IsAdmin
    @PutMapping("/{userId}")
    fun update(@PathVariable userId: Long, @RequestBody userDTO: UpdateUserDTO) {
        if(userDTO.username.isBlank()) throw BadRequestException() //TODO refactor validation
        userService.updateUser(userId, userDTO.username, nullIfBlank(userDTO.email), nullIfBlank(userDTO.password), userDTO.role)
    }

    @IsAdmin
    @DeleteMapping("/{userId}")
    fun delete(@PathVariable userId: Long, principal: Principal) {
        userService.deleteUser(userId, principal.name.toLongOrNull() ?: throw AccessDeniedException())
    }

    private fun nullIfBlank(string: String?): String? = string?.takeIf(String::isNotBlank)

    private fun User.toUserDTO() = UserDTO(id, username, email ?: "", role)
}