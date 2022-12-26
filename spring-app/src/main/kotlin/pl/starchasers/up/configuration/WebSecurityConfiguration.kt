package pl.starchasers.up.configuration

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
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
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${up.dev.cors}")
    private val devCors: Boolean = false

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf { it.disable() }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .addFilterBefore(JwtTokenFilter(jwtTokenService), UsernamePasswordAuthenticationFilter::class.java)
        .also {
            if (devCors) {
                logger.info("Development environment set. Enabling CORS for all origins.")
            }
            it.cors()
        }
        .build()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH")
//        configuration.allowCredentials = true
        configuration.applyPermitDefaultValues()
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
