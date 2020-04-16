package pl.starchasers.up.controller

import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.io.IOUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import pl.starchasers.up.data.dto.UploadCompleteResponseDTO
import pl.starchasers.up.exception.BadRequestException
import pl.starchasers.up.service.FileStorageService
import java.io.File
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class AnonymousUploadController(private val fileStorageService: FileStorageService) {

    //TODO return access token
    @PostMapping("/api/upload")
    fun anonymousUpload(request: HttpServletRequest): UploadCompleteResponseDTO {
        val isMultipart = ServletFileUpload.isMultipartContent(request)
        if (!isMultipart) throw BadRequestException()

        val upload = ServletFileUpload()
        val iterStream = upload.getItemIterator(request)

        val params = mutableMapOf<String, String>()
        var file: File? = null
        var filename: String = ""
        var contentType: String = ""

        while (iterStream.hasNext()) {
            val item = iterStream.next()
            val stream = item.openStream()
            if (!item.isFormField) {
                if (file != null) throw BadRequestException()//2 files in the same request
                if (item.fieldName != "file") throw BadRequestException()//incorrect field name

                file = fileStorageService.storeTemporaryFile(stream)
                filename = item.name
                contentType = item.contentType
            } else {
                params[item.fieldName]
            }
            stream.close()
        }

        val key = fileStorageService.storeNonPermanentFile(file
                ?: throw BadRequestException(), params, filename, contentType)
        return UploadCompleteResponseDTO(key, "")
    }

    @GetMapping("/u/{fileKey}")
    fun getAnonymousUpload(@PathVariable fileKey: String, response: HttpServletResponse) {
        val (fileEntry, stream) = fileStorageService.getStoredFileRaw(fileKey)

        response.contentType = fileEntry.contentType
        response.addHeader("Content-Disposition", "inline; filename=${fileEntry.filename}")

        try {
            IOUtils.copyLarge(stream, response.outputStream)
            response.outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            stream.close()
        }
    }

}