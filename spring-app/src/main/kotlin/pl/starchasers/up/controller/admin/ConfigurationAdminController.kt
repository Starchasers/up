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
        private val configurationService: ConfigurationService,
        private val userService: UserService
) {

    /**
     * Update one option in global configuration
     */
    @IsAdmin
    @PutMapping("")
    fun setConfigurationOption(@RequestBody configurationOptionDTO: ConfigurationOptionDTO) {
        configurationService.setConfigurationOption(configurationOptionDTO.key, configurationOptionDTO.value)
    }

    /**
     * Update many options in global configuration
     */
    @IsAdmin
    @PutMapping("/all")
    fun setConfiguration(@RequestBody configurationDTO: ConfigurationDTO) {
        configurationService.updateGlobalConfiguration(configurationDTO.options)
    }

    /**
     * Get entire global configuration
     */
    @IsAdmin
    @GetMapping("/all")
    fun getGlobalConfiguration(): ConfigurationDTO {
        return ConfigurationDTO(configurationService.getGlobalConfiguration())
    }

    //TODO move to UserAdminController (?)
    /**
     * Set user-specific limits
     * @param userId User to update
     */
    @IsAdmin
    @PutMapping("/user/{userId}")
    fun setUserConfiguration(@PathVariable userId: Long, @RequestBody userConfigurationDTO: UpdateUserConfigurationDTO) {
        val user = userService.findUser(userId) ?: throw NotFoundException()

        configurationService.updateUserConfiguration(user, userConfigurationDTO)
    }

    //TODO move to UserAdminController (?)
    /**
     * Get user-specific limits
     * @param userId User id
     */
    @IsAdmin
    @GetMapping("/user/{userId}")
    fun getUserConfiguration(@PathVariable userId: Long): UserConfigurationDTO {
        val user = userService.findUser(userId) ?: throw NotFoundException()

        return UserConfigurationDTO(
                user.maxTemporaryFileSize.value,
                user.maxFileLifetime.value,
                user.defaultFileLifetime.value,
                user.maxFileLifetime.value == 0L,
                user.maxPermanentFileSize.value
        )
    }
}