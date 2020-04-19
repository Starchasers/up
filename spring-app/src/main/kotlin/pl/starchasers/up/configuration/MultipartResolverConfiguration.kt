package pl.starchasers.up.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.multipart.commons.CommonsMultipartResolver





@Configuration
class MultipartResolverConfiguration(){
    @Bean(name = ["multipartResolver"])
    fun multipartResolver(): CommonsMultipartResolver? {
        val multipartResolver = CommonsMultipartResolver()
//        multipartResolver.setMaxUploadSize(100000)
        return multipartResolver
    }
}