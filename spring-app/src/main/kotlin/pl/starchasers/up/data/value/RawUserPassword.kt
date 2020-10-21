package pl.starchasers.up.data.value

import pl.starchasers.up.util.validate

data class RawUserPassword(
        val value: String
) {
    init {
        validate(this, RawUserPassword::value){
            check { it.isNotEmpty() }
        }
    }
}