package pl.starchasers.up.controller

import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.get
import pl.starchasers.up.DocumentResponse
import pl.starchasers.up.MockMvcTestBase
import pl.starchasers.up.OrderTests
import pl.starchasers.up.data.value.*
import pl.starchasers.up.responsePath
import pl.starchasers.up.service.FileService
import pl.starchasers.up.service.UserService
import javax.transaction.Transactional

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserControllerTest : MockMvcTestBase() {

    @Transactional
    @OrderTests
    @Nested
    inner class ListUserUploadHistory(
        @Autowired private val userService: UserService,
        @Autowired private val fileService: FileService
    ) : MockMvcTestBase() {

        private val listHistoryRequestPath = "/api/user/history"

        @DocumentResponse
        @Test
        fun `Given valid request, should return upload history`() {
            val file = fileService.createFile(
                "content".byteInputStream(),
                Filename("file"),
                ContentType("text/html"),
                FileSize(7),
                userService.getUser(Username("root"))
            )
            val fileEntry = fileService.findFileEntry(FileKey(file.key)) ?: throw IllegalStateException()
            mockMvc.get(listHistoryRequestPath) {
                cookie(getAdminAccessTokenCookie())
            }.andExpect {
                status { isOk() }
                content {
                    responsePath("$.content[0].filename", equalTo(fileEntry.filename.value))
                    responsePath("$.content[0].permanent", equalTo(fileEntry.permanent))
                    responsePath("$.content[0].size", equalTo(fileEntry.size.value))
                    responsePath("$.content[0].mimeType", equalTo(fileEntry.contentType.value))
                    responsePath("$.content[0].key", equalTo(fileEntry.key.value))
                }
            }
        }

        @Test
        fun `Given unauthenticated request, should return 403`() {
            mockMvc.get(listHistoryRequestPath).andExpect {
                status { isForbidden() }
            }
        }
    }
}
