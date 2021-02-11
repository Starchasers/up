package pl.starchasers.up.controller

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import pl.starchasers.up.exception.NotFoundException
import javax.servlet.http.HttpServletRequest

@Component
class NotFoundErrorViewResolver : ErrorViewResolver {

    override fun resolveErrorView(
        request: HttpServletRequest,
        status: HttpStatus,
        model: MutableMap<String, Any>
    ): ModelAndView {
        val path = model["path"]
        if (path is String && path.startsWith("/api/")) throw NotFoundException()

        return ModelAndView("forward:/index.html")
    }
}
