package pl.starchasers.up.controller

import org.springframework.beans.factory.annotation.Value
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

    @Value("\${up.max-file-size}")
    private val maxFileSize: Long = 0

    /**
     * Returns configuration for anonymous or logged in user, if Authorization header is provided
     */
    @GetMapping("/api/configuration")
    fun getConfiguration(principal: Principal?): UserConfigurationDTO {
        val user = userService.fromPrincipal(principal)

        //TODO 1.1 release workaround:
        //TODO go back to user configuration

//        return UserConfigurationDTO(
//                user?.maxTemporaryFileSize?.value ?: configurationService.getAnonymousMaxFileSize().value,
//                user?.maxFileLifetime?.value ?: configurationService.getAnonymousMaxFileLifetime().value,
//                user?.defaultFileLifetime?.value ?: configurationService.getAnonymousDefaultFileLifetime().value,
//                if (user != null) user.maxFileLifetime.value == 0L else configurationService.getAnonymousDefaultFileLifetime().value == 0L,
//                user?.maxPermanentFileSize?.value ?: if (configurationService.getAnonymousMaxFileLifetime().value == 0L)
//                    configurationService.getAnonymousMaxFileSize().value
//                else 0
//        )
        val oneDay: Long = 24 * 60 * 60 * 1000
        return UserConfigurationDTO(
            maxFileSize,
            oneDay,
            oneDay,
            false,
            0
        )
    }
}