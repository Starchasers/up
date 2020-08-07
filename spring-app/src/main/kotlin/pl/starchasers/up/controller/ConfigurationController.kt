package pl.starchasers.up.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.starchasers.up.data.dto.ConfigurationDTO
import pl.starchasers.up.service.ConfigurationService
import pl.starchasers.up.service.UserService
import java.lang.IllegalStateException
import java.security.Principal

@RestController
class ConfigurationController(
        private val configurationService: ConfigurationService,
        private val userService: UserService
) {

    @GetMapping("/api/configuration")
    fun getConfiguration(principal: Principal?): ConfigurationDTO {
        val user = if (principal != null) userService.findUser(principal.name.toLong()) else null

        return ConfigurationDTO(
                configurationService.getMaxTemporaryFileSize(user).value,
                configurationService.getMaxFileLifetime(user).value,
                configurationService.getDefaultFileLifetime(user).value,
                configurationService.isPermanentAllowed(user),
                configurationService.getMaxPermanentFileSize(user).value
        )
    }
}