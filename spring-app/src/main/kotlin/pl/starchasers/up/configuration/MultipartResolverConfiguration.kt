package pl.starchasers.up.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.multipart.commons.CommonsMultipartResolver

@Configuration
class MultipartResolverConfiguration() {
    @Value("\${up.max-file-size}")
    private val maxFileSize: Long = 0

    @Bean(name = ["multipartResolver"])
    fun multipartResolver(): CommonsMultipartResolver? {
        val multipartResolver = CommonsMultipartResolver()
        multipartResolver.setMaxUploadSize(maxFileSize)
        return multipartResolver
    }
}
