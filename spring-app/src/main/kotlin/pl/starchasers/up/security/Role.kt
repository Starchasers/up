package pl.starchasers.up.security

enum class Role {
    USER,
    ADMIN;

    fun roleString(): String = "ROLE_$this"
}