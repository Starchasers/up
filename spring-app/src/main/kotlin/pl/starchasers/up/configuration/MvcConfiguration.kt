package pl.starchasers.up.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.concurrent.TimeUnit

@Configuration
class MvcConfiguration : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/index.html")
            .addResourceLocations("classpath:static/")

        registry.addResourceHandler("/page-data/**")
            .addResourceLocations("classpath:static/page-data/")

        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:static/")
            .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))

        super.addResourceHandlers(registry)
    }
}