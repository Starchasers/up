package pl.starchasers.up.controller.admin

import org.springframework.web.bind.annotation.*
import pl.starchasers.up.data.dto.UserConfigurationDTO
import pl.starchasers.up.data.dto.configuration.ConfigurationOptionDTO
import pl.starchasers.up.security.IsAdmin

@RestController
@RequestMapping("/api/admin/config")
class ConfigurationAdminController() {

    @IsAdmin
    @PutMapping("/")
    fun setConfigurationOption(@RequestBody configurationOptionDTO: ConfigurationOptionDTO) {
        TODO()
    }

    @IsAdmin
    @GetMapping("/")
    fun getConfigurationOption(): ConfigurationOptionDTO {
        TODO()
    }

    @IsAdmin
    @GetMapping("/all")
    fun getGlobalConfiguration(): List<ConfigurationOptionDTO> {
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