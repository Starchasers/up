package pl.starchasers.up.controller

import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.starchasers.up.exception.BadRequestException
import pl.starchasers.up.service.FileStorageService
import java.io.File
import javax.servlet.http.HttpServletRequest

@RestController
class AnonymousUploadController(private val fileStorageService: FileStorageService) {

    //TODO return download link
    //TODO return access token
    @PostMapping("/api/upload")
    fun anonymousUpload(request: HttpServletRequest) {
        val isMultipart = ServletFileUpload.isMultipartContent(request)
        if (!isMultipart) throw BadRequestException()

        val upload = ServletFileUpload()
        val iterStream = upload.getItemIterator(request)

        val params = mutableMapOf<String, String>()
        var file: File? = null
        var filename: String = ""

        while (iterStream.hasNext()) {
            val item = iterStream.next()
            val stream = item.openStream()
            if (!item.isFormField) {
                if (file != null) throw BadRequestException()//2 files in the same request
                if (item.fieldName != "file") throw BadRequestException()//incorrect field name

                file = fileStorageService.storeTemporaryFile(stream)
                filename = item.name
            } else {
                params[item.fieldName]
            }
            stream.close()
        }

        fileStorageService.storeNonPermanentFile(file ?: throw BadRequestException(), params, filename)

    }

}