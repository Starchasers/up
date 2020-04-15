package pl.starchasers.up.controller

import org.apache.commons.fileupload.FileItemStream
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import pl.starchasers.up.service.FileStorageService
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/api/upload")
class AnonymousUploadController(private val fileStorageService: FileStorageService) {

    @PostMapping("/")
    fun anonymousUpload(request: HttpServletRequest) {
        val isMultipart = ServletFileUpload.isMultipartContent(request)
        //TODO check multipart

        val upload = ServletFileUpload()
        val iterStream = upload.getItemIterator(request)
        val item: FileItemStream = iterStream.next()

        //TODO check number of files uploaded
        if (!item.isFormField) fileStorageService.storeTemporaryFile(item.openStream())
    }

}