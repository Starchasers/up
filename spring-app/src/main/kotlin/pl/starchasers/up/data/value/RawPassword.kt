package pl.starchasers.up.data.value

import pl.starchasers.up.util.validate

data class RawPassword(
        val value: String
) {
    init {
        validate(this, RawPassword::value){
            check { it.isNotEmpty() }
        }
    }
}