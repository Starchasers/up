package pl.starchasers.up.controller

import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import pl.starchasers.up.data.dto.upload.AuthorizedOperationDTO
import pl.starchasers.up.data.dto.upload.FileDetailsDTO
import pl.starchasers.up.data.dto.upload.UploadCompleteResponseDTO
import pl.starchasers.up.data.value.*
import pl.starchasers.up.exception.AccessDeniedException
import pl.starchasers.up.exception.NotFoundException
import pl.starchasers.up.exception.NotFoundUIException
import pl.starchasers.up.service.FileService
import pl.starchasers.up.service.FileStorageService
import pl.starchasers.up.service.UserService
import pl.starchasers.up.util.BasicResponseDTO
import pl.starchasers.up.util.RequestRangeParser
import java.io.BufferedInputStream
import java.io.IOException
import java.nio.charset.Charset
import java.security.Principal
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class UploadController(
    private val fileStorageService: FileStorageService,
    private val fileService: FileService,
    private val requestRangeParser: RequestRangeParser,
    private val multipartResolver: CommonsMultipartResolver,
    private val userService: UserService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Upload new file
     * @param file Uploaded file content
     */
    @PostMapping("/api/upload")
    fun anonymousUpload(@RequestParam file: MultipartFile, principal: Principal?): UploadCompleteResponseDTO {
        val user = userService.fromPrincipal(principal)
        val contentType = ContentType(
            if (file.contentType == null || file.contentType!!.isBlank()) "application/octet-stream"
            else file.contentType!!
        )

        return fileService.createFile(
            BufferedInputStream(file.inputStream),
            Filename(file.originalFilename ?: "file"),
            contentType,
            FileSize(file.size),
            user
        )
    }

    /**
     * Download a previously uploaded file
     * @param fileKey File key obtained during upload
     */
    @GetMapping("/u/{fileKey}")
    fun getAnonymousUpload(@PathVariable fileKey: String, request: HttpServletRequest, response: HttpServletResponse) {
        val (fileEntry, stream) = try {
            fileStorageService.getStoredFileRaw(FileKey(fileKey))
        } catch (e: NotFoundException) {
            throw NotFoundUIException()
        }
        response.contentType = fileEntry.contentType.value

        response.addHeader(
            HttpHeaders.ACCEPT_RANGES,
            "bytes"
        )
        response.addHeader(
            HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition
                .builder("inline")
                .filename(fileEntry.filename.value.ifBlank { "file" }, Charset.forName("UTF-8"))
                .build()
                .toString()
        )
        try {
            val range = requestRangeParser(request.getHeader("Range"), fileEntry.size.value)

            if (range.partial) {
                response.addHeader(HttpHeaders.CONTENT_RANGE, "bytes ${range.from}-${range.to}/${fileEntry.size.value}")
                response.addHeader(HttpHeaders.CONTENT_LENGTH, range.responseSize.toString())
                response.status = HttpStatus.PARTIAL_CONTENT.value()
                IOUtils.copyLarge(stream, response.outputStream, range.from, range.responseSize)
            } else {
                response.addHeader(HttpHeaders.CONTENT_LENGTH, fileEntry.size.value.toString())
                IOUtils.copyLarge(stream, response.outputStream)
            }
            response.outputStream.flush()
        } catch (e: IOException) {
            logger.debug(e.toString())
        } finally {
            stream.close()
        }
    }

    /**
     * Verify requesting user's permission to modify this upload
     */
    @PostMapping("/api/u/{fileKey}/verify")
    fun verifyFileAccess(
        @PathVariable fileKey: String,
        @Validated @RequestBody operationDto: AuthorizedOperationDTO?,
        principal: Principal?
    ): BasicResponseDTO {
        val fileEntry = fileService.findFileEntry(FileKey(fileKey)) ?: throw NotFoundException()

        val user = userService.fromPrincipal(principal)
        if (!fileService.verifyFileAccess(fileEntry, operationDto?.accessToken?.let { FileAccessToken(it) }, user))
            throw AccessDeniedException()
        return BasicResponseDTO()
    }

    @DeleteMapping("/api/u/{fileKey}")
    fun deleteFile(
        @PathVariable fileKey: String,
        @Validated @RequestBody operationDto: AuthorizedOperationDTO?,
        principal: Principal?
    ) {
        val fileEntry = fileService.findFileEntry(FileKey(fileKey)) ?: throw NotFoundException()
        val user = userService.fromPrincipal(principal)

        if (!fileService.verifyFileAccess(fileEntry, operationDto?.accessToken?.let { FileAccessToken(it) }, user))
            throw AccessDeniedException()

        fileService.deleteFile(fileEntry)
    }

    /**
     * @return Uploaded file metadata
     */
    @GetMapping("/api/u/{fileKey}/details")
    fun getFileDetails(@PathVariable fileKey: String): FileDetailsDTO = fileService.getFileDetails(FileKey(fileKey))
}
