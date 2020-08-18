package pl.starchasers.up.controller.admin

import org.springframework.web.bind.annotation.*
import pl.starchasers.up.data.dto.configuration.UserConfigurationDTO
import pl.starchasers.up.data.dto.configuration.ConfigurationDTO
import pl.starchasers.up.data.dto.configuration.ConfigurationOptionDTO
import pl.starchasers.up.security.IsAdmin
import pl.starchasers.up.service.ConfigurationService

@RestController
@RequestMapping("/api/admin/config")
class ConfigurationAdminController(
        private val configurationService: ConfigurationService
) {

    @IsAdmin
    @PutMapping("")
    fun setConfigurationOption(@RequestBody configurationOptionDTO: ConfigurationOptionDTO) {
        configurationService.setConfigurationOption(configurationOptionDTO.key, configurationOptionDTO.value)
    }

//    @IsAdmin
//    @GetMapping("/")
//    fun getConfigurationOption(): ConfigurationOptionDTO {
//        TODO()
//    }

    @IsAdmin
    @PutMapping("/all")
    fun setConfiguration(@RequestBody configurationDTO: ConfigurationDTO) {
        configurationService.updateGlobalConfiguration(configurationDTO.options)
    }

    @IsAdmin
    @GetMapping("/all")
    fun getConfiguration(): ConfigurationDTO {
        TODO()
    }

    //TODO move to UserAdminController (?)
    @IsAdmin
    @PutMapping("/user/{userId}}")
    fun setUserConfiguration(@PathVariable userId: Long, @RequestBody userConfigurationDTO: UserConfigurationDTO) {
        TODO()
    }

    //TODO move to UserAdminController (?)
    @IsAdmin
    @GetMapping("/user/{userId}")
    fun getUserConfiguration(@PathVariable userId: Long): UserConfigurationDTO {
        TODO()
    }
}