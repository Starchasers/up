package pl.starchasers.up.util

import pl.starchasers.up.exception.ValidationException
import kotlin.reflect.KProperty1

inline fun <reified T, S> validate(obj: T, property: KProperty1<T, S>, validation: Validator<S>.() -> Unit) {
    val validator = Validator(property.get(obj))
    validation.invoke(validator)

    if (!validator.isValid)
        throw ValidationException(
            "Unable to validate object ${T::class.simpleName}, field ${property.name}. ${validator.errorMessage}"
        )
}

class Validator<S>(private val value: S) {

    var isValid = true
    var errorMessage: String = ""

    fun check(message: String = "False predicate.", predicate: (it: S) -> Boolean) {
        if (!predicate.invoke(value)) {
            isValid = false
            errorMessage = message
        }
    }
}
