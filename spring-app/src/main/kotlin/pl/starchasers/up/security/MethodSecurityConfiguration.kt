package pl.starchasers.up.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration

@Configuration
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    prePostEnabled = true
)
class MethodSecurityConfiguration : GlobalMethodSecurityConfiguration()
