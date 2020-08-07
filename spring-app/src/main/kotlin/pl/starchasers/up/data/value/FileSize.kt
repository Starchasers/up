package pl.starchasers.up.data.value


data class FileSize(
        val value: Long
) {
    init {
        require(value >= 0)
    }
}