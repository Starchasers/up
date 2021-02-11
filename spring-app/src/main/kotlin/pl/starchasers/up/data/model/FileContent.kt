package pl.starchasers.up.data.model

import pl.starchasers.up.data.value.FileKey
import java.io.InputStream

class FileContent(
    val key: FileKey,
    val data: InputStream
)
