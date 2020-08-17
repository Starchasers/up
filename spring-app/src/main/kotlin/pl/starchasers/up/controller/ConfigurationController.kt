package pl.starchasers.up.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.starchasers.up.data.dto.configuration.UserConfigurationDTO
import pl.starchasers.up.service.ConfigurationService
import pl.starchasers.up.service.UserService
import java.security.Principal

@RestController
class ConfigurationController(
        private val configurationService: ConfigurationService,
        private val userService: UserService
) {

    /**
     * Returns configuration for anonymous or logged in user, if Authorization header is provided
     */
    @GetMapping("/api/configuration")
    fun getConfiguration(principal: Principal?): UserConfigurationDTO {
        val user = if (principal != null) userService.findUser(principal.name.toLong()) else null

        return UserConfigurationDTO(
                user?.maxTemporaryFileSize?.value ?: configurationService.getAnonymousMaxFileSize().value,
                user?.maxFileLifetime?.value ?: configurationService.getAnonymousMaxFileLifetime().value,
                user?.defaultFileLifetime?.value ?: configurationService.getAnonymousDefaultFileLifetime().value,
                if (user != null) user.maxFileLifetime.value == 0L else configurationService.getAnonymousDefaultFileLifetime().value == 0L,
                user?.maxPermanentFileSize?.value ?: configurationService.getAnonymousMaxFileSize().value
        )
    }
}