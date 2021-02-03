package pl.starchasers.up.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.starchasers.up.data.model.User
import pl.starchasers.up.data.value.Username

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findFirstByUsername(username: Username): User?

    fun findFirstById(id: Long): User?
}
