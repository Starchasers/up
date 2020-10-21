package pl.starchasers.up.service

import com.ibm.icu.text.CharsetDetector
import org.springframework.stereotype.Service
import java.io.InputStream

interface CharsetDetectionService {
    fun detect(inputStream: InputStream): String
}

@Service
class CharsetDetectionServiceImpl : CharsetDetectionService {
    override fun detect(inputStream: InputStream): String {
        val detector = CharsetDetector()
        detector.setDeclaredEncoding("utf-8")
        detector.setText(inputStream)
        return detector.detect().name
    }

}