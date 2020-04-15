package pl.starchasers.up.data.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class FileEntry(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int
)