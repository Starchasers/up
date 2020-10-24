package pl.starchasers.up.data.value

import pl.starchasers.up.util.validate
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class FileAccessToken(
    @Column(name = "fileAccessToken", length = 128)
        val value: String
){
    init {
        validate(this, FileAccessToken::value){
            check("Token too long.") { it.length <= 128 }
            check("Token cannot be blank.") { it.isNotBlank() }
        }
    }
}