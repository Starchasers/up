package pl.starchasers.up.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.multipart.MultipartResolver
import org.springframework.web.multipart.support.StandardServletMultipartResolver

@Configuration
class MultipartResolverConfiguration() {

    @Bean(name = ["multipartResolver"])
    fun multipartResolver(): MultipartResolver? {
        val multipartResolver = StandardServletMultipartResolver()
        multipartResolver.setResolveLazily(true)
        return multipartResolver
    }
}
