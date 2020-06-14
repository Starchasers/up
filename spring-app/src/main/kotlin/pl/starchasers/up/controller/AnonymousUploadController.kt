package pl.starchasers.up.controller

import org.apache.commons.io.IOUtils
import org.springframework.http.ContentDisposition
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pl.starchasers.up.data.dto.upload.*
import pl.starchasers.up.exception.AccessDeniedException
import pl.starchasers.up.exception.NotFoundException
import pl.starchasers.up.service.FileService
import pl.starchasers.up.service.FileStorageService
import pl.starchasers.up.util.BasicResponseDTO
import java.io.IOException
import java.nio.charset.Charset
import javax.servlet.http.HttpServletResponse

@RestController
class AnonymousUploadController(private val fileStorageService: FileStorageService,
                                private val fileService: FileService) {

    /**
     * @param file Uploaded file content
     */
    @PostMapping("/api/upload")
    fun anonymousUpload(@RequestParam file: MultipartFile): UploadCompleteResponseDTO =
            fileService.createFile(file.inputStream,
                    file.originalFilename ?: "file",
                    file.contentType ?: "application/octet-stream",
                    file.size)

    @PostMapping("/api/verifyUpload")
    fun verifyUploadSize(@RequestBody verifyUploadSizeDTO: VerifyUploadSizeDTO): VerifyUploadSizeResponseDTO {
        return fileService.verifyUploadSize(verifyUploadSizeDTO.size)
    }

    /**
     * @param fileKey File key obtained during upload
     */
    @GetMapping("/u/{fileKey}")
    fun getAnonymousUpload(@PathVariable fileKey: String, response: HttpServletResponse) {
        val (fileEntry, stream) = fileStorageService.getStoredFileRaw(fileKey)
        response.contentType = fileEntry.contentType

        response.addHeader("Content-Disposition",
                ContentDisposition
                        .builder("inline")
                        .filename(fileEntry.filename, Charset.forName("UTF-8"))
                        .build()
                        .toString())
        try {
            IOUtils.copyLarge(stream, response.outputStream)
            response.outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            stream.close()
        }
    }

    @PostMapping("/api/u/{fileKey}/verify")
    fun verifyFileAccess(@PathVariable fileKey: String,
                         @Validated @RequestBody operationDto: AuthorizedOperationDTO
    ): BasicResponseDTO {
        val fileEntry = fileService.findFileEntry(fileKey) ?: throw NotFoundException()

        if (!fileService.verifyFileAccess(fileEntry, operationDto.accessToken)) throw AccessDeniedException()
        return BasicResponseDTO()
    }

    @GetMapping("/api/u/{fileKey}/details")
    fun getFileDetails(@PathVariable fileKey: String): FileDetailsDTO = fileService.getFileDetails(fileKey)

}