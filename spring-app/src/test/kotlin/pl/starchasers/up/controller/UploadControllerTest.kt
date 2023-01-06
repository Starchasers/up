package pl.starchasers.up.controller

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.apache.commons.fileupload.util.Streams
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.multipart
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.configuration.UpdateUserConfigurationDTO
import pl.starchasers.up.data.dto.upload.FileDetailsDTO
import pl.starchasers.up.data.dto.upload.UploadCompleteResponseDTO
import pl.starchasers.up.data.value.*
import pl.starchasers.up.repository.FileEntryRepository
import pl.starchasers.up.repository.UploadRepository
import pl.starchasers.up.security.Role
import pl.starchasers.up.service.ConfigurationService
import pl.starchasers.up.service.FileService
import pl.starchasers.up.service.UserService
import java.time.LocalDateTime

internal class UploadControllerTest : JpaTestBase() {

    @Nested
    inner class AnonymousUpload(
        @Autowired private val fileEntryRepository: FileEntryRepository,
        @Autowired private val uploadRepository: UploadRepository,
        @Autowired private val userService: UserService,
        @Autowired private val configurationService: ConfigurationService
    ) : MockMvcTestBase() {

        private val requestPath = "/api/upload"

        private fun getExampleTextFile(contentType: String = "text/plain; charset=UTF-8") = MockMultipartFile(
            "file",
            "exampleTextFile.txt",
            contentType,
            "example content".toByteArray()
        )

        // TODO integrate access token

        @Test
        fun `Given valid request, should upload and store file`() {
            val response: UploadCompleteResponseDTO = mockMvc.multipart(requestPath) {
                file(getExampleTextFile())
            }.andExpect {
                status { isOk() }
            }.andReturn().parse()

            val fileEntry = fileEntryRepository.findAll()[0]
            with(response) {
                key shouldBe fileEntry.key.value
                accessToken shouldBe fileEntry.accessToken.value
                toDelete.shouldNotBeNull()
            }

            with(fileEntry) {
                contentType.value shouldBe "text/plain; charset=UTF-8"
                encrypted shouldBe false
                filename.value shouldBe "exampleTextFile.txt"
                password.shouldBeNull()
                permanent shouldBe false
                toDeleteDate.shouldNotBeNull()
                fileEntry.toDeleteDate!!.toLocalDateTime().isAfter(LocalDateTime.now()) shouldBe true
            }

            uploadRepository.find(fileEntry.key)?.let { fileContent ->
                assertEquals("example content", Streams.asString(fileContent.data))
            } ?: throw RuntimeException()
        }

        @Test
        fun `Given missing file, should return 400`() {
            mockMvc.multipart(requestPath)
                .andExpect {
                    status { isBadRequest() }
                }
        }

        @Test
        fun `Given missing file content type, should save file as application octet-stream`() {
            val exampleTextFile = MockMultipartFile(
                "file",
                "exampleTextFile.txt",
                "",
                "example content".toByteArray()
            )

            val response: UploadCompleteResponseDTO = mockMvc.multipart(requestPath) {
                file(getExampleTextFile(contentType = ""))
            }.andExpect {
                status { isOk() }
            }.andReturn().parse()

            val fileEntry = fileEntryRepository.findAll()[0]
            with(response) {
                key shouldBe fileEntry.key.value
                accessToken shouldBe fileEntry.accessToken.value
                toDelete.shouldNotBeNull()
            }

            with(fileEntry) {
                fileEntry.contentType.value shouldBe "application/octet-stream"
                encrypted shouldBe false
                filename.value shouldBe "exampleTextFile.txt"
                password.shouldBeNull()
                permanent shouldBe false
                toDeleteDate.shouldNotBeNull()
                toDeleteDate!!.toLocalDateTime().isAfter(LocalDateTime.now()) shouldBe true
            }

            uploadRepository.find(fileEntry.key)?.let { fileContent ->
                assertEquals("example content", Streams.asString(fileContent.data))
            } ?: throw RuntimeException()
        }

        @Test
        fun `Given too large file, should return 413`() {
            val testUser = userService.createUser(Username("testUser"), RawPassword("password"), null, Role.USER)
            configurationService.updateUserConfiguration(
                testUser,
                UpdateUserConfigurationDTO(
                    10,
                    testUser.maxFileLifetime.value,
                    testUser.defaultFileLifetime.value,
                    testUser.maxPermanentFileSize.value
                )
            )

            mockMvc.multipart(requestPath) {
                authorizeAsUser(testUser)
                file(getExampleTextFile())
            }.andExpect {
                status { isPayloadTooLarge() }
            }
        }
    }

    @Nested
    inner class GetAnonymousUpload(
        @Autowired val fileService: FileService
    ) : MockMvcTestBase() {
        private val content = "example content"

        private val requestPath = "/u/{key}"

        private fun createFile(contentType: String, fileContent: String = content): String = fileService.createFile(
            fileContent.byteInputStream(),
            Filename("fileName.txt"),
            ContentType(contentType),
            FileSize(fileContent.byteInputStream().readAllBytes().size.toLong()),
            null
        ).key

        @Test
        fun `Given valid key, should return raw file`() {
            val key = createFile(contentType = "application/octet-stream")

            val response: String = mockMvc.get(requestPath, key).andExpect {
                status { status { isOk() } }
                header {
                    string(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    string(HttpHeaders.CONTENT_LENGTH, "${content.length}")
                }
            }.andReturn().response.contentAsString

            response shouldBe content
        }

        @Test
        fun `Given valid Range header, should return 206`() {
            val contentSize = content.byteInputStream().readAllBytes().size.toLong()
            val key = createFile("text/plain")

            mockMvc.get(requestPath, key) {
                header(HttpHeaders.RANGE, "bytes=0-")
            }.andExpect {
                status { isPartialContent() }
                header {
                    string(HttpHeaders.CONTENT_RANGE, "bytes 0-${contentSize - 1}/$contentSize")
                    string(HttpHeaders.CONTENT_LENGTH, "$contentSize")
                }
            }
        }

        @Test
        fun `Given invalid Range header, should return 200`() {
            val key = createFile("text/plain")

            val headers = HttpHeaders()
            headers.set(HttpHeaders.RANGE, "mb=-1024")

            mockMvc.get(requestPath, key) {
                header(HttpHeaders.RANGE, "mb=-1024")
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `Given incorrect key, should return 404`() {
            mockMvc.get(requestPath, "qweasd").andExpect {
                status { isNotFound() }
            }
        }

        @Test
        fun `Given unspecified text file encoding, should guess based on content`() {
            val fileContent = "Ā ā Ă অ আ ই ঈ উ"
            val key = createFile("text/plain", fileContent = fileContent)

            val response: String = mockMvc.get(requestPath, key).andExpect {
                status { isOk() }
                header {
                    string("Content-Type", "text/plain; charset=UTF-8")
                }
            }.andReturn().response.contentAsString

            response shouldBe fileContent
        }

        @Test
        fun `Given specified text file encoding, should preserve it`() {
            val contentType = "text/plain; charset=us-ascii"
            val key = createFile(contentType = contentType)

            val response: String = mockMvc.get(requestPath, key).andExpect {
                status { isOk() }
                header {
                    string("Content-Type", contentType)
                }
            }.andReturn().response.contentAsString

            response shouldBe content
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class VerifyFileAccess(
        @Autowired val fileService: FileService,
        @Autowired val fileEntryRepository: FileEntryRepository,
        @Autowired val userService: UserService
    ) : MockMvcTestBase() {
        private val requestPath = "/api/u/{key}/verify"
        private val content = "example content"

        private lateinit var fileKey: String
        private lateinit var fileAccessToken: String

        @BeforeEach
        fun setup() {
            fileKey = fileService.createFile(
                content.byteInputStream(),
                Filename("filename.txt"),
                ContentType("text/plain"),
                FileSize(content.byteInputStream().readAllBytes().size.toLong()),
                userService.getUser(Username("root"))
            ).key

            fileAccessToken = fileEntryRepository.findExistingFileByKey(FileKey(fileKey))?.accessToken?.value
                ?: throw IllegalStateException()
        }

        @Test
        fun `Given valid access token, should return 200`() {
            mockMvc.postJson(requestPath, fileKey) {
                jsonContent = object {
                    val accessToken = fileAccessToken
                }
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `Given valid owner and no token, should return 200`() {
            mockMvc.postJson(requestPath, fileKey) {
                authorizeAsAdmin()
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `Given invalid owner and valid token, should return 200`() {
            mockMvc.postJson(requestPath, fileKey) {
                jsonContent = object {
                    val accessToken = fileAccessToken
                }
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `Given invalid access token, should return 403`() {
            mockMvc.postJson(requestPath, fileKey) {
                jsonContent = object {
                    val accessToken = "qweasd"
                }
            }.andExpect {
                status { isForbidden() }
            }
        }

        @Test
        fun `Given missing access token and no user, should return 403`() {
            mockMvc.postJson(requestPath, fileKey) {
            }.andExpect {
                status { isForbidden() }
            }
        }

        @Test
        fun `Given invalid file key, should return 404`() {
            mockMvc.postJson(requestPath, "qweasd") {
                jsonContent = object {
                    val accessToken = fileAccessToken
                }
            }.andExpect {
                status { isNotFound() }
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetFileDetails(
        @Autowired val fileService: FileService
    ) : MockMvcTestBase() {

        private val requestPath = "/api/u/{key}/details"
        private val content = "example content"
        private lateinit var fileKey: String
        private val filename: String = "filename.txt"
        private val contentType: String = "text/plain; charset=UTF-8"

        @BeforeEach
        fun setup() {
            fileKey = fileService.createFile(
                content.byteInputStream(),
                Filename(filename),
                ContentType(contentType),
                FileSize(content.byteInputStream().readAllBytes().size.toLong()),
                null
            ).key
        }

        @Test
        fun `Given correct key, should return file details`() {
            val response: FileDetailsDTO = mockMvc.get(requestPath, fileKey) {
            }.andExpect {
                status { isOk() }
            }.andReturn().parse()

            with(response) {
                key shouldBe fileKey
                name shouldBe filename
                permanent shouldBe false // TODO support permanent files
                size shouldBe content.byteInputStream().readAllBytes().size.toLong()
                type shouldBe contentType
            }
        }

        @Test
        fun `Given incorrect key, should return 404`() {
            mockMvc.get(requestPath, "incorrectKey") {
            }.andExpect {
                status { isNotFound() }
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteFile(
        @Autowired val fileService: FileService,
        @Autowired val uploadRepository: UploadRepository,
        @Autowired val fileEntryRepository: FileEntryRepository,
        @Autowired val userService: UserService
    ) : MockMvcTestBase() {

        private val requestPath = "/api/u/{key}"

        private fun createTestFile(): UploadCompleteResponseDTO {
            val fileContent = "fileContent"
            return fileService.createFile(
                fileContent.byteInputStream(),
                Filename("file"),
                ContentType("text/plain"),
                FileSize(fileContent.length.toLong()),
                userService.getUser(Username("root"))
            )
        }

        @Test
        fun `Given valid access token, should delete file`() {
            val response = createTestFile()
            mockMvc.deleteJson(requestPath, response.key) {
                jsonContent = object {
                    val accessToken = response.accessToken
                }
            }.andExpect {
                status { isOk() }
            }

            assertNull(uploadRepository.find(FileKey(response.key)))
            assertNull(fileEntryRepository.findExistingFileByKey(FileKey(response.key)))
        }

        @Test
        fun `Given valid owner, should delete file`() {
            val response = createTestFile()

            mockMvc.deleteJson(requestPath, response.key) {
                authorizeAsAdmin()
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `Given wrong access token, should return 403`() {
            val response = createTestFile()

            mockMvc.deleteJson(requestPath, response.key) {
                jsonContent = object {
                    val accessToken = "forSureNotRealToken"
                }
            }.andExpect {
                status { isForbidden() }
            }

            assertNotNull(uploadRepository.find(FileKey(response.key)))
            assertNotNull(fileEntryRepository.findExistingFileByKey(FileKey(response.key)))
        }

        @Test
        fun `Given not existing file, should return 404`() {
            val response = createTestFile()

            mockMvc.deleteJson(requestPath, "qwe") {
                jsonContent = object {
                    val accessToken = response.accessToken
                }
            }.andExpect {
                status { isNotFound() }
            }

            assertNotNull(uploadRepository.find(FileKey(response.key)))
            assertNotNull(fileEntryRepository.findExistingFileByKey(FileKey(response.key)))
        }
    }
}
