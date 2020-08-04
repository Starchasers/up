package pl.starchasers.up.controller

import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pl.starchasers.up.data.dto.VerifyUploadSizeDTO
import pl.starchasers.up.data.dto.VerifyUploadSizeResponseDTO
import pl.starchasers.up.data.dto.upload.AuthorizedOperationDTO
import pl.starchasers.up.data.dto.upload.FileDetailsDTO
import pl.starchasers.up.data.dto.upload.UploadCompleteResponseDTO
import pl.starchasers.up.exception.AccessDeniedException
import pl.starchasers.up.exception.NotFoundException
import pl.starchasers.up.service.FileService
import pl.starchasers.up.service.FileStorageService
import pl.starchasers.up.util.BasicResponseDTO
import pl.starchasers.up.util.RequestRangeParser
import java.io.IOException
import java.nio.charset.Charset
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class AnonymousUploadController(private val fileStorageService: FileStorageService,
                                private val fileService: FileService,
                                private val requestRangeParser: RequestRangeParser) {

    private val logger = LoggerFactory.getLogger(this::class.java)

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
    fun getAnonymousUpload(@PathVariable fileKey: String, request: HttpServletRequest, response: HttpServletResponse) {
        val (fileEntry, stream) = fileStorageService.getStoredFileRaw(fileKey)
        response.contentType = fileEntry.contentType

        response.addHeader("Content-Disposition",
                ContentDisposition
                        .builder("inline")
                        .filename(fileEntry.filename.ifBlank { "file" }, Charset.forName("UTF-8"))
                        .build()
                        .toString())
        response.addHeader("Content-Length", fileEntry.size.toString())
        try {
            val range = requestRangeParser(request.getHeader("Range"), fileEntry.size)

            if (range.partial) {
                response.addHeader(HttpHeaders.CONTENT_RANGE, "bytes ${range.from}-${range.to}/${fileEntry.size}")
                response.addHeader(HttpHeaders.CONTENT_LENGTH, range.responseSize.toString())
                response.status = HttpStatus.PARTIAL_CONTENT.value()
                IOUtils.copyLarge(stream, response.outputStream, range.from, range.responseSize)
            } else {
                IOUtils.copyLarge(stream, response.outputStream)
            }
            response.outputStream.flush()
        } catch (e: IOException) {
            logger.debug(e.toString())
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