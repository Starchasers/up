package pl.starchasers.up.util

import java.lang.IllegalArgumentException
import java.security.SecureRandom

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

inline fun <T, U> T?.ifNotNull(function: (obj: T) -> U) {
    if (this != null) function.invoke(this)
}

fun <T, U> T?.runOrNull(function: (obj: T) -> U?): U? = if (this == null) null else function.invoke(this)
