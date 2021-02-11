package pl.starchasers.up.util

import org.springframework.security.crypto.password.PasswordEncoder
import pl.starchasers.up.data.value.RawPassword
import pl.starchasers.up.data.value.UserPassword

fun PasswordEncoder.encode(password: RawPassword): UserPassword = UserPassword(this.encode(password.value))

fun PasswordEncoder.matches(password: RawPassword, actualPassword: UserPassword) =
    this.matches(password.value, actualPassword.value)
