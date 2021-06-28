package pl.starchasers.up.util

/**
 * https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie
 */
class SetCookieHeaderValueBuilder(name: String, value: String) {

    private var builder: StringBuilder = StringBuilder()

    init {
        withName(name)
        withValue(value)
    }

    private fun withName(name: String): SetCookieHeaderValueBuilder {
        builder.append(name)
        return this
    }

    private fun withValue(value: String): SetCookieHeaderValueBuilder {
        builder.append("=").append(value)
        return this
    }

    fun withMaxAge(seconds: Number): SetCookieHeaderValueBuilder {
        builder.append("; ").append("Max-Age=").append(seconds)
        return this
    }

    fun withPath(path: String): SetCookieHeaderValueBuilder {
        builder.append("; ").append("Path=").append(path)
        return this
    }

    fun sameSite(option: String = "Strict"): SetCookieHeaderValueBuilder {
        builder.append("; ").append("SameSite=").append(option)
        return this
    }

    fun secure(): SetCookieHeaderValueBuilder {
        builder.append("; ").append("Secure")
        return this
    }

    fun httpOnly(): SetCookieHeaderValueBuilder {
        builder.append("; ").append("HttpOnly")
        return this
    }

    fun build(): String {
        if (builder.isEmpty()) throw IllegalStateException("Cookie can't be empty")
        return builder.toString()
    }
}
