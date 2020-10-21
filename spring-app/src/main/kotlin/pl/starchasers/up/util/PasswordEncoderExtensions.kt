package pl.starchasers.up.util

import org.springframework.security.crypto.password.PasswordEncoder
import pl.starchasers.up.data.value.RawUserPassword
import pl.starchasers.up.data.value.UserPassword

fun PasswordEncoder.encode(password: RawUserPassword): UserPassword = UserPassword(this.encode(password.value))

fun PasswordEncoder.matches(password: RawUserPassword, actualPassword: UserPassword) =
        this.matches(password.value, actualPassword.value)