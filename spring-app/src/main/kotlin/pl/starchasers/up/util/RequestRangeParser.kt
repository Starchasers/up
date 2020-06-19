package pl.starchasers.up.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pl.starchasers.up.exception.BadRangeException

interface RequestRangeParser {
    operator fun invoke(header: String?, fileSize: Long): RequestRange
}

@Service
class RequestRangeParserImpl : RequestRangeParser {

    @Value("\${up.chunk-size}")
    private val chunkSize: Long = 0
    private val units = setOf("bytes")

    override fun invoke(header: String?, fileSize: Long): RequestRange {
        val fullFile = RequestRange(
                from = 0, to = fileSize, responseSize = fileSize, partial = false
        )
        header ?: return fullFile
        if (!units.contains(header.split('=')[0])) return fullFile

        val parsedHeader = header.removePrefix("bytes=").split("-")

        val from = parsedHeader[0].toLong()
        val to = if (parsedHeader[1].isEmpty()) {
            java.lang.Long.min(from + chunkSize, fileSize - 1)
        } else {
            parsedHeader[1].toLong()
        }
        val responseSize = to - from + 1

        if (from >= fileSize) throw BadRangeException(fileSize)

        return RequestRange(
                from = from,
                to = to,
                responseSize = responseSize,
                partial = true
        )
    }
}

data class RequestRange(
        val from: Long,
        val to: Long,
        val responseSize: Long,
        val partial: Boolean
)