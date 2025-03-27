package pl.starchasers.up.data.model

import java.io.InputStream

class FileContent(
    val key: FileKey,
    val data: InputStream
)
