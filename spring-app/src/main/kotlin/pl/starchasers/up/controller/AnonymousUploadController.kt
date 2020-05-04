package pl.starchasers.up.controller

import org.apache.commons.io.IOUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartFile
import pl.starchasers.up.data.dto.UploadCompleteResponseDTO
import pl.starchasers.up.service.FileStorageService
import java.io.IOException
import javax.servlet.http.HttpServletResponse

@RestController
class AnonymousUploadController(private val fileStorageService: FileStorageService) {

    /**
     * @param file Uploaded file content
     */
    //TODO return access token
    @PostMapping("/api/upload")
    fun anonymousUpload(@RequestParam file: MultipartFile): UploadCompleteResponseDTO {
        val key = fileStorageService.storeNonPermanentFile(file.inputStream, file.originalFilename
                ?: "file", file.contentType
                ?: "application/octet-stream")
        return UploadCompleteResponseDTO(key, "")
    }

    /**
     * @param fileKey File key obtained during upload
     */
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