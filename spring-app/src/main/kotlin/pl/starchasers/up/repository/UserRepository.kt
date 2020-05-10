package pl.starchasers.up.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.starchasers.up.data.model.User

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findFirstByUsername(username: String): User?

    fun findFirstById(id: Int): User?
}