package pl.starchasers.up.configuration

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import pl.starchasers.up.security.JwtTokenFilter
import pl.starchasers.up.service.JwtTokenService


@Configuration
@EnableWebSecurity
class WebSecurityConfiguration(
        private val jwtTokenService: JwtTokenService
) : WebSecurityConfigurerAdapter() {

    private val logger = LoggerFactory.getLogger(WebSecurityConfiguration::class.java)

    @Value("\${up.dev.cors}")
    private val devCors:Boolean = false

    override fun configure(web: HttpSecurity) {
        http
                .csrf().disable()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.addFilterBefore(
                JwtTokenFilter(jwtTokenService),
                UsernamePasswordAuthenticationFilter::class.java
        )

        if(devCors){
            logger.info("Development environment set. Enabling CORS for all origins.")
            http.cors()
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
//        configuration.allowCredentials = true
        configuration.applyPermitDefaultValues()
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}