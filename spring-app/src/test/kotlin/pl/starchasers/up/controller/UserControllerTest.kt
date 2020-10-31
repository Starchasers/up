package pl.starchasers.up.controller

import no.skatteetaten.aurora.mockmvc.extensions.Path
import no.skatteetaten.aurora.mockmvc.extensions.authorization
import no.skatteetaten.aurora.mockmvc.extensions.get
import no.skatteetaten.aurora.mockmvc.extensions.responseJsonPath
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import pl.starchasers.up.*
import pl.starchasers.up.data.value.*
import pl.starchasers.up.service.FileService
import pl.starchasers.up.service.UserService
import java.lang.IllegalStateException
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

        private val listHistoryRequestPath = Path("/api/user/history")

        @DocumentResponse
        @Test
        fun `Given valid request, should return upload history`(){
            val file = fileService.createFile(
                    "content".byteInputStream(),
                    Filename("file"),
                    ContentType("text/html"),
                    FileSize(7),
                    userService.getUser(Username("root"))
            )
            val fileEntry = fileService.findFileEntry(FileKey(file.key)) ?: throw IllegalStateException()
            mockMvc.get(path = listHistoryRequestPath,
            headers = HttpHeaders().authorization(getAdminAccessToken())){
                isSuccess()
                responseJsonPath("$.content[0].filename").equalsValue(fileEntry.filename.value)
                responseJsonPath("$.content[0].permanent").equalsValue(fileEntry.permanent)
                responseJsonPath("$.content[0].size").equalsValue(fileEntry.size.value)
                responseJsonPath("$.content[0].mimeType").equalsValue(fileEntry.contentType.value)
                responseJsonPath("$.content[0].key").equalsValue(fileEntry.key.value)
            }
        }

        @Test
        fun `Given unauthenticated request, should return 403`(){
            mockMvc.get(path = listHistoryRequestPath){
                isError(HttpStatus.FORBIDDEN)
            }
        }
    }
}