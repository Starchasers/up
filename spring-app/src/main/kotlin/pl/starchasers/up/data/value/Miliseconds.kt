package pl.starchasers.up.data.value

data class Miliseconds(
        val value: Long
) {
    init {
        require(value > 0)
    }
}