package pl.starchasers.up.util

open class BasicErrorResponseDTO(
    /**
     * Present only when error occured and success=false
     */
    val errorMessage: String? = "Error occurred!",

    /**
     * True only when request was completed successfully
     */
    val success: Boolean = false
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BasicErrorResponseDTO

        if (errorMessage != other.errorMessage) return false
        if (success != other.success) return false

        return true
    }

    override fun hashCode(): Int {
        var result = errorMessage?.hashCode() ?: 0
        result = 31 * result + success.hashCode()
        return result
    }
}