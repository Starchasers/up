package pl.starchasers.up.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
class UtilController {

    @Value("\${up.domain}")
    private val domain: String = ""

    @GetMapping("/sharex")
    fun sharexConfig(): String = """
        {
          "Version": "13.1.0",
          "Name": "UP file hosting",
          "DestinationType": "ImageUploader, TextUploader, FileUploader",
          "RequestMethod": "POST",
          "RequestURL": "${domain}/api/upload",
          "Body": "MultipartFormData",
          "FileFormName": "file",
          "URL": "${domain}/u/${'$'}json:key${'$'}"
        }
    """.trimIndent()

    @GetMapping("/admin")
    fun adminPanel(): ModelAndView {
        return ModelAndView("admin/index.html")
    }

}
