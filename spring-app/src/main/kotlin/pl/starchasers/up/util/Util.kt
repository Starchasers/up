package pl.starchasers.up.util

import java.security.SecureRandom
import java.time.Duration
import java.time.Instant
import java.time.temporal.TemporalUnit

class Util() {
    private val random = SecureRandom()

    private val readableCharacters = "abcdefghijkmnprstuvwxyzABCDEFGHJKLMNPRSTUVWXYZ123456789"
    private val alphanumericCharacters = "abcdefghijklmnoprstuvwxyzABCDEFGHIJKLMNOPRSTUVWXYZ1234567890"

    fun secureReadableRandomString(length: Int): String = secureRandomString(readableCharacters, length)

    fun secureAlphanumericRandomString(length: Int): String = secureRandomString(alphanumericCharacters, length)

    private fun secureRandomString(characters: String, length: Int): String {
        if (length < 1) throw IllegalArgumentException("String length < 1: $length")
        return String(CharArray(length) { characters[random.nextInt(characters.length)] })
    }

}

fun isCloseTo(secondsEpoh: Long, amount: Long, unit: TemporalUnit): Boolean {
    val now = Instant.now()
    val exp = Instant.ofEpochSecond(secondsEpoh)
    return (Duration.between(exp, now) < Duration.of(amount, unit))
}
