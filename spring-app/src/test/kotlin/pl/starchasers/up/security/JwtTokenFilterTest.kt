package pl.starchasers.up.security

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.starchasers.up.service.JwtTokenService
import pl.starchasers.up.util.isCloseTo
import java.time.Instant
import java.time.temporal.ChronoUnit

@SpringBootTest
class JwtTokenFilterTest(
    @Autowired private val tokenService: JwtTokenService
) {

    @Test
    fun `isCloseTo should return true`() {
        assert(
            isCloseTo(
                Instant.now().minusSeconds(1).toEpochMilli() / 1000, 2, ChronoUnit.SECONDS
            )
        )
        assert(
            isCloseTo(
                Instant.now().minusSeconds(600).toEpochMilli() / 1000, 1000, ChronoUnit.SECONDS
            )
        )
        assert(
            isCloseTo(
                Instant.now().minusSeconds(60).toEpochMilli() / 1000, 2, ChronoUnit.MINUTES
            )
        )
        assert(
            isCloseTo(
                Instant.now().minusSeconds(60 * 60 * 24).toEpochMilli() / 1000, 2, ChronoUnit.DAYS
            )
        )
        assert(
            isCloseTo(
                Instant.now().plusSeconds(60).toEpochMilli() / 1000, 2, ChronoUnit.MINUTES
            )
        )
    }

    @Test
    fun `isCloseTo should return false`() {
        assert(
            !isCloseTo(
                Instant.now().minusSeconds(60).toEpochMilli() / 1000, 10, ChronoUnit.SECONDS
            )
        )
        assert(
            !isCloseTo(
                Instant.now().minusSeconds(600).toEpochMilli() / 1000, 500, ChronoUnit.SECONDS
            )
        )
        assert(
            !isCloseTo(
                Instant.now().minusSeconds(120).toEpochMilli() / 1000, 1, ChronoUnit.MINUTES
            )
        )
        assert(
            !isCloseTo(
                Instant.now().minusSeconds(60 * 60 * 24).toEpochMilli() / 1000, 2, ChronoUnit.MINUTES
            )
        )
    }
}
