package pl.starchasers.up.controller

import no.skatteetaten.aurora.mockmvc.extensions.Path
import no.skatteetaten.aurora.mockmvc.extensions.get
import no.skatteetaten.aurora.mockmvc.extensions.responseHeader
import no.skatteetaten.aurora.mockmvc.extensions.responseJsonPath
import org.apache.commons.fileupload.util.Streams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.annotation.DirtiesContext
import pl.starchasers.up.*
import pl.starchasers.up.repository.FileEntryRepository
import pl.starchasers.up.repository.UploadRepository
import pl.starchasers.up.service.FileStorageService
import javax.transaction.Transactional


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
internal class AnonymousUploadControllerTest() : MockMvcTestBase() {

    @Transactional
    @OrderTests
    @Nested
    inner class AnonymousUpload(
            @Autowired val fileEntryRepository: FileEntryRepository,
            @Autowired val uploadRepository: UploadRepository) : MockMvcTestBase() {

        private val uploadFileRequestPath = Path("/api/upload")

        //TODO integrate access token

        @DocumentResponse
        @Test
        fun `Given valid request, should upload and store file`() {
            val exampleTextFile = MockMultipartFile("file",
                    "exampleTextFile.txt",
                    "text/plain",
                    "example content".toByteArray())

            mockMvc.multipart(path = uploadFileRequestPath,
                    fnBuilder = {
                        file(exampleTextFile)
                    }) {
                isSuccess()
                fileEntryRepository.findAll()[0].let { fileEntry ->
                    responseJsonPath("$.key").equalsValue(fileEntry.key)
                }
            }

            fileEntryRepository.findAll()[0].let { fileEntry ->
                assertEquals("text/plain", fileEntry.contentType)
                assertEquals(false, fileEntry.encrypted)
                assertEquals("exampleTextFile.txt", fileEntry.filename)
                assertEquals(null, fileEntry.password)
                assertEquals(false, fileEntry.permanent)

                uploadRepository.find(fileEntry.key)?.let { fileContent ->
                    assertEquals("example content", Streams.asString(fileContent.data))
                } ?: throw RuntimeException()
            }
        }

        @Test
        fun `Given missing file, should return 400`() {
            mockMvc.multipart(path = uploadFileRequestPath, fnBuilder = {}) {
                isError(HttpStatus.BAD_REQUEST)
            }
        }

        @Test
        fun `Given missing file content type, should save file as application octet-stream`() {
            val exampleTextFile = MockMultipartFile("file",
                    "exampleTextFile.txt",
                    "",
                    "example content".toByteArray())

            mockMvc.multipart(path = uploadFileRequestPath,
                    fnBuilder = {
                        file(exampleTextFile)
                    }) {
                isSuccess()
                fileEntryRepository.findAll()[0].let { fileEntry ->
                    responseJsonPath("$.key").equalsValue(fileEntry.key)
                }
            }

            fileEntryRepository.findAll()[0].let { fileEntry ->
                assertEquals("application/octet-stream", fileEntry.contentType)
                assertEquals(false, fileEntry.encrypted)
                assertEquals("exampleTextFile.txt", fileEntry.filename)
                assertEquals(null, fileEntry.password)
                assertEquals(false, fileEntry.permanent)

                uploadRepository.find(fileEntry.key)?.let { fileContent ->
                    assertEquals("example content", Streams.asString(fileContent.data))
                } ?: throw RuntimeException()
            }
        }
    }

    @Transactional
    @OrderTests
    @Nested
    inner class GetAnonymousUpload(
            @Autowired val fileStorageService: FileStorageService
    ) : MockMvcTestBase() {

        @Test
        @DocumentResponse
        fun `Given valid key, should return raw file`() {
            val key = fileStorageService.storeNonPermanentFile(
                    "example content".byteInputStream(),
                    "fileName.txt",
                    "text/plain"
            )

            mockMvc.get(path = Path("/u/$key")) {
                responseJsonPath("$").equalsValue("example content")
                responseHeader("Content-Type").equals("text/plain")
            }

        }

        @Test
        fun `Given incorrect key, should return 404`() {
            mockMvc.get(path = Path("/u/qweasd")) {
                isError(HttpStatus.NOT_FOUND)
            }
        }
    }
}