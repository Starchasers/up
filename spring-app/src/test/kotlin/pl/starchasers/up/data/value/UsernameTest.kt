package pl.starchasers.up.data.value

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import pl.starchasers.up.exception.ValidationException

internal class UsernameTest {

    @Test
    fun `should allow valid usernames`() {
        val username = Username("exampleUsername123")
        assertEquals("exampleUsername123", username.value)
    }

    @Test
    fun `given blank username, should throw ValidationException`() {
        assertThrows<ValidationException> { Username("") }
    }

    @Test
    fun `given username with whitespace, should throw ValidationException`() {
        assertThrows<ValidationException> { Username("qwe asd") }
    }

    @Test
    fun `given too long username, should throw ValidationException`() {
        assertThrows<ValidationException> { Username("a".repeat(33)) }
    }
}
