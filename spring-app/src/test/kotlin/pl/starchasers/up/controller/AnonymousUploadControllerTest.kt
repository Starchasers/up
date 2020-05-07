package pl.starchasers.up.controller

import no.skatteetaten.aurora.mockmvc.extensions.*
import org.apache.commons.fileupload.util.Streams
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.annotation.DirtiesContext
import pl.starchasers.up.*
import pl.starchasers.up.repository.FileEntryRepository
import pl.starchasers.up.repository.UploadRepository
import pl.starchasers.up.service.FileService
import java.lang.IllegalStateException
import java.time.LocalDateTime
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
                    responseJsonPath("$.accessToken").equalsValue(fileEntry.accessToken)
                    responseJsonPath("$.toDelete").isNotEmpty()
                }
            }

            fileEntryRepository.findAll()[0].let { fileEntry ->
                assertEquals("text/plain", fileEntry.contentType)
                assertEquals(false, fileEntry.encrypted)
                assertEquals("exampleTextFile.txt", fileEntry.filename)
                assertEquals(null, fileEntry.password)
                assertEquals(false, fileEntry.permanent)
                assertTrue(fileEntry.accessToken.isNotBlank())
                assertNotNull(fileEntry.toDeleteDate)
                assertTrue(fileEntry.toDeleteDate!!.toLocalDateTime().isAfter(LocalDateTime.now()))

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
                    responseJsonPath("$.accessToken").equalsValue(fileEntry.accessToken)
                    responseJsonPath("$.toDelete").isNotEmpty()
                }
            }

            fileEntryRepository.findAll()[0].let { fileEntry ->
                assertEquals("application/octet-stream", fileEntry.contentType)
                assertEquals(false, fileEntry.encrypted)
                assertEquals("exampleTextFile.txt", fileEntry.filename)
                assertEquals(null, fileEntry.password)
                assertEquals(false, fileEntry.permanent)
                assertTrue(fileEntry.accessToken.isNotBlank())
                assertNotNull(fileEntry.toDeleteDate)
                assertTrue(fileEntry.toDeleteDate!!.toLocalDateTime().isAfter(LocalDateTime.now()))

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
            @Autowired val fileService: FileService
    ) : MockMvcTestBase() {
        private val content = "example content"

        @Test
        @DocumentResponse
        fun `Given valid key, should return raw file`() {
            val key = fileService.createFile(
                    content.byteInputStream(),
                    "fileName.txt",
                    "text/plain",
                    content.byteInputStream().readAllBytes().size.toLong()
            ).key

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

    @Transactional
    @OrderTests
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class VerifyFileAccess(
            @Autowired val fileService: FileService,
            @Autowired val fileEntryRepository: FileEntryRepository
    ) : MockMvcTestBase() {
        private fun verifyRequestPath(key: String): Path = Path("/api/u/$key/verify")
        private val content = "example content"

        private lateinit var fileKey: String;
        private lateinit var fileAccessToken: String;

        @BeforeAll
        fun setup() {
            fileKey = fileService.createFile(content.byteInputStream(),
                    "filename.txt",
                    "text/plain",
                    content.byteInputStream().readAllBytes().size.toLong()).key

            fileAccessToken = fileEntryRepository.findExistingFileByKey(fileKey)?.accessToken
                    ?: throw IllegalStateException()
        }

        @Test
        @DocumentResponse
        fun `Given valid access token, should return 200`() {
            mockMvc.post(path = verifyRequestPath(fileKey),
                    headers = HttpHeaders().contentTypeJson(),
                    body = mapper.writeValueAsString(object {
                        val accessToken = fileAccessToken
                    })) {
                isSuccess()
            }
        }

        @Test
        fun `Given invalid access token, should return 403`() {
            mockMvc.post(path = verifyRequestPath(fileKey),
                    headers = HttpHeaders().contentTypeJson(),
                    body = object {
                        val accessToken = fileAccessToken + "qwe"
                    }) {
                isError(HttpStatus.FORBIDDEN)
            }
        }

        @Test
        fun `Given missing access token, should return 400`() {
            mockMvc.post(path = verifyRequestPath(fileKey),
                    headers = HttpHeaders().contentTypeJson(),
                    body = object {}) {
                isError(HttpStatus.BAD_REQUEST)
            }
        }

        @Test
        fun `Given invalid file key, should return 404`() {
            mockMvc.post(path = verifyRequestPath("qweasd"),
                    headers = HttpHeaders().contentTypeJson(),
                    body = object {
                        val accessToken = fileAccessToken
                    }) {
                isError(HttpStatus.NOT_FOUND)
            }
        }

    }

    @Transactional
    @OrderTests
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetFileDetails(
            @Autowired val fileService: FileService,
            @Autowired val fileEntryRepository: FileEntryRepository
    ) : MockMvcTestBase() {

        private fun getRequestPath(key: String): Path = Path("/api/u/$key/details")
        private val content = "example content"
        private lateinit var fileKey: String;
        private val filename: String = "filename.txt"
        private val contentType: String = "text/plain"

        @BeforeAll
        fun setup() {
            fileKey = fileService.createFile(content.byteInputStream(),
                    filename,
                    contentType,
                    content.byteInputStream().readAllBytes().size.toLong()).key
        }

        @Test
        @DocumentResponse
        fun `Given correct key, should return file details`() {
            mockMvc.get(path = getRequestPath(fileKey)) {
                responseJsonPath("$.key").equalsValue(fileKey)
                responseJsonPath("$.name").equalsValue(filename)
                responseJsonPath("$.permanent").equalsValue(false)//TODO support permanent files
                responseJsonPath("$.expirationDate").isNotEmpty()//TODO fix objectMapper
                responseJsonPath("$.size").equalsValue(content.byteInputStream().readAllBytes().size.toLong())
                responseJsonPath("$.type").equalsValue(contentType)
            }
        }

        @Test
        fun `Given incorrect key, should return 404`() {
            mockMvc.get(path = getRequestPath("incorrectKey")) {
                isError(HttpStatus.NOT_FOUND)
            }
        }
    }
}