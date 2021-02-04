package pl.starchasers.up.util

open class BasicResponseDTO : BasicErrorResponseDTO(null, true) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false
        return true
    }
}
