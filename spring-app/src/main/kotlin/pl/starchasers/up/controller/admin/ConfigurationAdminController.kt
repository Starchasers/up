package pl.starchasers.up.controller.admin

import org.springframework.web.bind.annotation.*
import pl.starchasers.up.data.dto.configuration.UserConfigurationDTO
import pl.starchasers.up.data.dto.configuration.ConfigurationDTO
import pl.starchasers.up.data.dto.configuration.ConfigurationOptionDTO
import pl.starchasers.up.data.dto.configuration.UpdateUserConfigurationDTO
import pl.starchasers.up.exception.NotFoundException
import pl.starchasers.up.security.IsAdmin
import pl.starchasers.up.service.ConfigurationService
import pl.starchasers.up.service.UserService

@RestController
@RequestMapping("/api/admin/config")
class ConfigurationAdminController(
        private val configurationService: ConfigurationService
) {

    /**
     * Update many options in global configuration
     */
    @IsAdmin
    @PutMapping("")
    fun updateConfiguration(@RequestBody configurationDTO: ConfigurationDTO) {
        configurationService.updateGlobalConfiguration(configurationDTO.options)
    }

    /**
     * Get entire global configuration
     */
    @IsAdmin
    @GetMapping("")
    fun getAppConfiguration(): ConfigurationDTO {
        return ConfigurationDTO(configurationService.getGlobalConfiguration())
    }

}