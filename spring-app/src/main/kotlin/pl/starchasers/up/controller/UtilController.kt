package pl.starchasers.up.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UtilController {

    @Value("\${up.domain}")
    private val domain: String = ""

    @GetMapping("/sharex")
    fun sharexConfig(): String = """
        {
          "Version": "13.1.0",
          "Name": "up.snet.ovh",
          "DestinationType": "ImageUploader, TextUploader, FileUploader",
          "RequestMethod": "POST",
          "RequestURL": "https://up.snet.ovh/api/upload",
          "Body": "MultipartFormData",
          "FileFormName": "file",
          "URL": "${domain}/u/${'$'}json:key${'$'}"
        }
    """.trimIndent()

}
