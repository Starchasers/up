package pl.starchasers.up.controller

import org.apache.commons.fileupload.util.Streams
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.post
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.configuration.UpdateUserConfigurationDTO
import pl.starchasers.up.data.dto.upload.UploadCompleteResponseDTO
import pl.starchasers.up.data.value.*
import pl.starchasers.up.repository.FileEntryRepository
import pl.starchasers.up.repository.UploadRepository
import pl.starchasers.up.security.Role
import pl.starchasers.up.service.ConfigurationService
import pl.starchasers.up.service.FileService
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.service.UserService
import java.time.LocalDateTime
import javax.servlet.http.Cookie

internal class UploadControllerTest : JpaTestBase() {

    @OrderTests
    @Nested
    inner class AnonymousUpload(
        @Autowired private val fileEntryRepository: FileEntryRepository,
        @Autowired private val uploadRepository: UploadRepository,
        @Autowired private val userService: UserService,
        @Autowired private val configurationService: ConfigurationService,
        @Autowired private val jwtTokenService: JwtTokenService
    ) : MockMvcTestBase() {

        private val uploadFileRequestPath = "/api/upload"

        // TODO integrate access token

        @DocumentResponse
        @Test
        fun `Given valid request, should upload and store file`() {
            val exampleTextFile = MockMultipartFile(
                "file",
                "exampleTextFile.txt",
                "text/plain; charset=UTF-8",
                "example content".toByteArray()
            )

            mockMvc.multipart(uploadFileRequestPath) {
                file(exampleTextFile)
            }.andExpect {
                status { isOk() }
                fileEntryRepository.findAll()[0].let { fileEntry ->
                    content {
                        responsePath("$.key", equalTo(fileEntry.key.value))
                        responsePath("$.accessToken", equalTo(fileEntry.accessToken.value))
                        responsePath("$.toDelete", notNullValue())
                    }
                }
            }

            fileEntryRepository.findAll()[0].let { fileEntry ->
                assertEquals("text/plain; charset=UTF-8", fileEntry.contentType.value)
                assertEquals(false, fileEntry.encrypted)
                assertEquals("exampleTextFile.txt", fileEntry.filename.value)
                assertEquals(null, fileEntry.password)
                assertEquals(false, fileEntry.permanent)
                assertNotNull(fileEntry.toDeleteDate)
                assertTrue(fileEntry.toDeleteDate!!.toLocalDateTime().isAfter(LocalDateTime.now()))

                uploadRepository.find(fileEntry.key)?.let { fileContent ->
                    assertEquals("example content", Streams.asString(fileContent.data))
                } ?: throw RuntimeException()
            }
        }

        @Test
        fun `Given missing file, should return 400`() {
            mockMvc.multipart(uploadFileRequestPath).andExpect {
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

            mockMvc.multipart(uploadFileRequestPath) {
                file(exampleTextFile)
            }.andExpect {
                status { is2xxSuccessful() }
                fileEntryRepository.findAll()[0].let { fileEntry ->
                    content {
                        responsePath("$.key", equalTo(fileEntry.key.value))
                        responsePath("$.accessToken", equalTo(fileEntry.accessToken.value))
                        responsePath("$.toDelete", notNullValue())
                    }
                }
            }

            fileEntryRepository.findAll()[0].let { fileEntry ->
                assertEquals("application/octet-stream", fileEntry.contentType.value)
                assertEquals(false, fileEntry.encrypted)
                assertEquals("exampleTextFile.txt", fileEntry.filename.value)
                assertEquals(null, fileEntry.password)
                assertEquals(false, fileEntry.permanent)
                assertNotNull(fileEntry.toDeleteDate)
                assertTrue(fileEntry.toDeleteDate!!.toLocalDateTime().isAfter(LocalDateTime.now()))

                uploadRepository.find(fileEntry.key)?.let { fileContent ->
                    assertEquals("example content", Streams.asString(fileContent.data))
                } ?: throw RuntimeException()
            }
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

            val accessToken = jwtTokenService.issueAccessToken(jwtTokenService.issueRefreshToken(testUser))
            val exampleTextFile = MockMultipartFile(
                "file",
                "exampleTextFile.txt",
                "text/plain",
                "example content".toByteArray()
            )
            mockMvc.multipart(uploadFileRequestPath) {
                headers { contentTypeJson() }
                cookie(Cookie(JwtTokenService.ACCESS_TOKEN_COOKIE_NAME, accessToken))
                file(exampleTextFile)
            }.andExpect {
                status { isPayloadTooLarge() }
            }
        }
    }

    @OrderTests
    @Nested
    inner class GetAnonymousUpload(
        @Autowired val fileService: FileService
    ) : MockMvcTestBase() {
        private val content = "example content"

        private fun createFile(contentType: String, fileContent: String = content): String = fileService.createFile(
            fileContent.byteInputStream(),
            Filename("fileName.txt"),
            ContentType(contentType),
            FileSize(fileContent.byteInputStream().readAllBytes().size.toLong()),
            null
        ).key

        @Test
        @DocumentResponse
        fun `Given valid key, should return raw file`() {
            val key = createFile("application/octet-stream")

            mockMvc.get("/u/$key")
                .andExpect {
                    status { isOk() }
                    header {
                        string(
                            HttpHeaders.CONTENT_TYPE,
                            equalTo(MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        )
                    }
                    header { string(HttpHeaders.CONTENT_LENGTH, equalTo("${content.length}")) }
                    content { responsePath("$", equalTo("example content")) }
                }
        }

        @Test
        fun `Given valid Range header, should return 206`() {
            val contentSize = content.byteInputStream().readAllBytes().size.toLong()
            val key = createFile("text/plain")

            mockMvc.get("/u/$key") {
                headers { set(HttpHeaders.RANGE, "bytes=0-") }
            }.andExpect {
                status { isPartialContent() }
                header {
                    string(
                        HttpHeaders.CONTENT_RANGE,
                        equalTo("bytes 0-${contentSize - 1}/$contentSize")
                    )
                }
                header { string(HttpHeaders.CONTENT_LENGTH, equalTo("$contentSize")) }
            }
        }

        @Test
        fun `Given invalid Range header, should return 200`() {
            val key = createFile("text/plain")

            mockMvc.get("/u/$key") {
                headers { set(HttpHeaders.RANGE, "mb=-1024") }
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `Given incorrect key, should return 404`() {
            mockMvc.get("/u/qweasd").andExpect {
                status { isNotFound() }
            }
        }

        @Test
        fun `Given unspecified text file encoding, should guess based on content`() {
            val content = "Ā ā Ă অ আ ই ঈ উ"
            val key = createFile("text/plain", fileContent = content)

            mockMvc.get("/u/$key").andExpect {
                status { isOk() }
                content { responsePath("$", equalTo(content)) }
                header { string(HttpHeaders.CONTENT_TYPE, equalTo("text/plain; charset=UTF-8")) }
            }
        }

        @Test
        fun `Given specified text file encoding, should preserve it`() {
            val contentType = "text/plain; charset=us-ascii"
            val key = createFile(contentType)

            mockMvc.get("/u/$key").andExpect {
                status { isOk() }
                content { responsePath("$", equalTo("example content")) }
                header { string(HttpHeaders.CONTENT_TYPE, equalTo(contentType)) }
            }
        }
    }

    @OrderTests
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class VerifyFileAccess(
        @Autowired val fileService: FileService,
        @Autowired val fileEntryRepository: FileEntryRepository,
        @Autowired val userService: UserService
    ) : MockMvcTestBase() {
        private fun verifyRequestPath(key: String) = "/api/u/$key/verify"
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
        @DocumentResponse
        fun `Given valid access token, should return 200`() {
            mockMvc.post(verifyRequestPath(fileKey)) {
                headers { contentTypeJson() }
                content(object {
                    val accessToken = fileAccessToken
                })
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `Given valid owner and no token, should return 200`() {
            mockMvc.post(verifyRequestPath(fileKey)) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `Given invalid owner and valid token, should return 200`() {
            mockMvc.post(verifyRequestPath(fileKey)) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
                content(object {
                    val accessToken = fileAccessToken
                })
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `Given invalid access token, should return 403`() {
            mockMvc.post(verifyRequestPath(fileKey)) {
                headers { contentTypeJson() }
                content(object {
                    val accessToken = "qweasd"
                })
            }.andExpect {
                status { isForbidden() }
            }
        }

        @Test
        fun `Given missing access token and no user, should return 403`() {
            mockMvc.post(verifyRequestPath(fileKey)) {
                headers { contentTypeJson() }
            }.andExpect {
                status { isForbidden() }
            }
        }

        @Test
        fun `Given invalid file key, should return 404`() {
            mockMvc.post(verifyRequestPath("qweasd")) {
                headers { contentTypeJson() }
                content(object {
                    val accessToken = fileAccessToken
                })
            }.andExpect {
                status { isNotFound() }
            }
        }
    }

    @OrderTests
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetFileDetails(
        @Autowired val fileService: FileService
    ) : MockMvcTestBase() {

        private fun getRequestPath(key: String) = "/api/u/$key/details"
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
        @DocumentResponse
        fun `Given correct key, should return file details`() {
            mockMvc.get(getRequestPath(fileKey)).andExpect {
                content {
                    responsePath("$.key", equalTo(fileKey))
                    responsePath("$.name", equalTo(filename))
                    responsePath("$.permanent", equalTo(false)) // TODO support permanent files
                    responsePath("$.expirationDate", notNullValue()) // TODO fix objectMapper
                    responsePath("$.size", equalTo(content.byteInputStream().readAllBytes().size.toLong()))
                    responsePath("$.type", equalTo(contentType))
                }
            }
        }

        @Test
        fun `Given incorrect key, should return 404`() {
            mockMvc.get(getRequestPath("incorrectKey")).andExpect {
                status { isNotFound() }
            }
        }
    }

    @OrderTests
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteFile(
        @Autowired val fileService: FileService,
        @Autowired val uploadRepository: UploadRepository,
        @Autowired val fileEntryRepository: FileEntryRepository,
        @Autowired val userService: UserService
    ) : MockMvcTestBase() {

        private fun getRequestPath(fileKey: String) = "/api/u/$fileKey"

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
        @DocumentResponse
        fun `Given valid access token, should delete file`() {
            val response = createTestFile()

            mockMvc.delete(getRequestPath(response.key)) {
                headers { contentTypeJson() }
                content(object {
                    val accessToken = response.accessToken
                })
            }.andExpect {
                status { is2xxSuccessful() }
            }

            assertNull(uploadRepository.find(FileKey(response.key)))
            assertNull(fileEntryRepository.findExistingFileByKey(FileKey(response.key)))
        }

        @Test
        fun `Given valid owner, should delete file`() {
            val response = createTestFile()

            mockMvc.delete(getRequestPath(response.key)) {
                headers { contentTypeJson() }
                cookie(getAdminAccessTokenCookie())
            }.andExpect {
                status { is2xxSuccessful() }
            }
        }

        @Test
        fun `Given wrong access token, should return 403`() {
            val response = createTestFile()

            mockMvc.delete(getRequestPath(response.key)) {
                headers { contentTypeJson() }
                content(object {
                    val accessToken = "realTokenNoScamHere"
                })
            }.andExpect {
                status { isForbidden() }
            }

            assertNotNull(uploadRepository.find(FileKey(response.key)))
            assertNotNull(fileEntryRepository.findExistingFileByKey(FileKey(response.key)))
        }

        @Test
        fun `Given not existing file, should return 404`() {
            val response = createTestFile()

            mockMvc.delete(getRequestPath("qweasd")) {
                headers { contentTypeJson() }
                content(object {
                    val accessToken = response.accessToken
                })
            }.andExpect {
                status { isNotFound() }
            }

            assertNotNull(uploadRepository.find(FileKey(response.key)))
            assertNotNull(fileEntryRepository.findExistingFileByKey(FileKey(response.key)))
        }
    }
}
