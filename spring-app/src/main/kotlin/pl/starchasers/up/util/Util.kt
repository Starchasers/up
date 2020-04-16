package pl.starchasers.up.util

import java.lang.IllegalArgumentException
import java.security.SecureRandom

class Util() {
    private val random = SecureRandom()

    private val characters = "abcdefghijkmnprstuvwxyzABCDEFGHJKLMNPRSTUVWXYZ123456789"

    fun secureRandomString(length: Int): String {
        if (length < 1) throw IllegalArgumentException("String length < 1: $length")
        return String(CharArray(length) { characters[random.nextInt(characters.length)] })
    }
}