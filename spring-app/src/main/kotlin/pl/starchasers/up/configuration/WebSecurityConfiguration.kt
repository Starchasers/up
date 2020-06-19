package pl.starchasers.up.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import pl.starchasers.up.security.JwtTokenFilter
import pl.starchasers.up.service.JwtTokenService

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration(
        private val jwtTokenService: JwtTokenService
) : WebSecurityConfigurerAdapter() {

    override fun configure(web: HttpSecurity) {
        http.csrf().disable()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.addFilterBefore(
                JwtTokenFilter(jwtTokenService),
                UsernamePasswordAuthenticationFilter::class.java
        )
    }
}