package pl.starchasers.up.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ShellUtilController(
    @Value("\${up.domain}")
    private val domain: String
) {
    @GetMapping("/sh")
    fun bashUploadUtil(): String {
        return getBashUploadScript()
    }

    private fun getBashUploadScript() =
        """
        #!/bin/bash
        if [ -f $1 ]; then
            echo $domain/u/$(curl '$domain/api/upload' -X POST \
            -H 'Content-Type: multipart/form-data' \
            -F "file=@$1" | grep -Po '"key":"\K[a-zA-Z0-9]*')
        else
            echo 'Invalid file path'
            echo 'Usage: bash <(curl $domain/sh) fileToUpload'
        fi
        """.trimIndent()
}
