package pl.starchasers.up.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.starchasers.up.data.dto.upload.UploadHistoryEntryDTO
import pl.starchasers.up.exception.AccessDeniedException
import pl.starchasers.up.security.IsUser
import pl.starchasers.up.service.FileService
import pl.starchasers.up.service.UserService
import java.security.Principal

@RestController
@RequestMapping("/api/user")
class UserController(
        val fileService: FileService,
        val userService: UserService
) {


    @GetMapping("/history")
    @IsUser
    fun listUserUploadHistory(principal: Principal, pageable: Pageable): Page<UploadHistoryEntryDTO> {
        return fileService.getUploadHistory(
                userService.fromPrincipal(principal) ?: throw AccessDeniedException(),
                pageable
        ).map {
            UploadHistoryEntryDTO(
                    it.filename.value,
                    it.createdDate,
                    it.permanent,
                    it.toDeleteDate,
                    it.size.value,
                    it.contentType.value,
                    it.key.value
            )
        }
    }
}