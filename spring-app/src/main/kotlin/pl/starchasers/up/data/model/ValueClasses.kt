package pl.starchasers.up.data.model

@JvmInline
value class FileId(val value: Long) {
    override fun toString(): String {
        return value.toString()
    }
}

@JvmInline
value class FileKey(val value: String) {
    override fun toString(): String {
        return value.toString()
    }
}

@JvmInline
value class FilePassword(val value: String) {
    override fun toString(): String {
        return value.toString()
    }
}

@JvmInline
value class FileAccessToken(val value: String) {
    override fun toString(): String {
        return value.toString()
    }
}

// Always in bytes
@JvmInline
value class FileSize(val value: Long) {
    override fun toString(): String {
        return value.toString()
    }
}

@JvmInline
value class FileName(val value: String) {
    override fun toString(): String {
        return value.toString()
    }
}
