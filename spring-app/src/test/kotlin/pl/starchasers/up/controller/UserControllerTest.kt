package pl.starchasers.up.controller

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.get
import org.springframework.transaction.annotation.Transactional
import pl.starchasers.up.*
import pl.starchasers.up.data.dto.upload.UploadHistoryEntryDTO
import pl.starchasers.up.data.value.*
import pl.starchasers.up.service.FileService
import pl.starchasers.up.service.UserService
import java.lang.IllegalStateException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserControllerTest : MockMvcTestBase() {

    @Transactional
    @Nested
    inner class ListUserUploadHistory(
        @Autowired private val userService: UserService,
        @Autowired private val fileService: FileService
    ) : MockMvcTestBase() {

        private val requestPath = "/api/user/history"

        // TODO enable test after migration to postgres #212
        // @Test
        fun `Given valid request, should return upload history`() {
            val file = fileService.createFile(
                "content".byteInputStream(),
                Filename("file"),
                ContentType("text/html"),
                FileSize(7),
                userService.getUser(Username("root"))
            )
            val fileEntry = fileService.findFileEntry(FileKey(file.key)) ?: throw IllegalStateException()

            val response: Page<UploadHistoryEntryDTO> = mockMvc.get(requestPath) {
                authorizeAsAdmin()
            }.andExpect {
                status { isOk() }
            }.andReturn().parse()

            with(response.content[0]) {
                filename shouldBe fileEntry.filename.value
                permanent shouldBe fileEntry.permanent
                size shouldBe fileEntry.size.value
                mimeType shouldBe fileEntry.contentType.value
                key shouldBe fileEntry.key.value
            }
        }

        @Test
        fun `Given unauthenticated request, should return 403`() {
            mockMvc.get(requestPath)
                .andExpect {
                    status { isForbidden() }
                }
        }
    }
}
