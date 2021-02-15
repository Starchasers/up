package pl.starchasers.up.data.dto.upload

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import pl.starchasers.up.exception.BadRequestException
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.abs

internal class FileLifetimeConverterTest {

    @Test
    fun `should accept milliseconds`() {
        // given
        val converter = FileLifetimeConverter()

        // when
        val duration = converter.convert("100")!!.duration

        // then
        assertEquals(Duration.ofMillis(100), duration)
    }

    @Test
    fun `should accept hours`() {
        // given
        val converter = FileLifetimeConverter()

        // when
        val duration = converter.convert("10h")!!.duration

        // then
        assertAlmostEqual(Duration.ofHours(10), duration)
    }

    @Test
    fun `should accept days`() {
        // given
        val converter = FileLifetimeConverter()

        // when
        val duration = converter.convert("10d")!!.duration

        // then
        assertAlmostEqual(Duration.ofDays(10), duration)
    }

    @Test
    fun `should accept weeks`() {
        // given
        val converter = FileLifetimeConverter()

        // when
        val duration = converter.convert("2w")!!.duration

        // then
        assertAlmostEqual(Duration.ofDays(14), duration)
    }

    @Test
    fun `should accept months`() {
        // given
        val converter = FileLifetimeConverter()

        // when
        val duration = converter.convert("2m")!!.duration

        // then
        assertAlmostEqual(Duration.between(LocalDateTime.now(), LocalDateTime.now().plusMonths(2)), duration)
    }

    @Test
    fun `should accept years`() {
        // given
        val converter = FileLifetimeConverter()

        // when
        val duration = converter.convert("2y")!!.duration

        // then
        assertAlmostEqual(Duration.between(LocalDateTime.now(), LocalDateTime.now().plusYears(2)), duration)
    }

    @Test
    fun `should not accept negative times`() {
        // given
        val converter = FileLifetimeConverter()

        // when
        // then
        assertThrows<BadRequestException> {
            converter.convert("-10")
        }

        assertThrows<BadRequestException> {
            converter.convert("-10d")
        }
    }

    @Test
    fun `should not accept illegal characters`() {
        // given
        val converter = FileLifetimeConverter()

        // when
        // then
        assertThrows<BadRequestException> {
            converter.convert("12q3")
        }
    }

    @Test
    fun `should not accept illegal units`() {
        // given
        val converter = FileLifetimeConverter()

        // when
        // then
        assertThrows<BadRequestException> {
            converter.convert("10k")
        }
    }

    @Test
    fun `should not accept blank`() {
        // given
        val converter = FileLifetimeConverter()

        // when
        // then
        assertThrows<BadRequestException> {
            converter.convert("")
        }
    }

    @Test
    fun `should not accept blank with unit`() {
        // given
        val converter = FileLifetimeConverter()

        // when
        // then
        assertThrows<BadRequestException> {
            converter.convert("d")
        }
    }

    private fun assertAlmostEqual(expected: Duration, actual: Duration) {
        assertTrue(
            abs(expected.toMillis() - actual.toMillis()) < 1000
        )
    }
}
