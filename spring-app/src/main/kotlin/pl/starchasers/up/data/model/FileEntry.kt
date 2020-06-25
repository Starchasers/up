package pl.starchasers.up.data.model

import java.sql.Timestamp
import javax.persistence.*

@Entity
class FileEntry(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(name = "fileKey", nullable = false, unique = false, length = 32)
        val key: String,

        @Column(nullable = false, unique = false, length = 1024)
        val filename: String,

        @Column(nullable = false, unique = false, length = 256)
        val contentType: String,

        @Column(nullable = true, unique = false)
        val password: String?,

        @Column(nullable = false, unique = false)
        val encrypted: Boolean,

        @Column(nullable = false, unique = false)
        val createdDate: Timestamp,

        @Column(nullable = true, unique = false)
        val toDeleteDate: Timestamp?,

        @Column(nullable = false, unique = false)
        val permanent: Boolean,

        @Column(nullable = false, unique = false, length = 128)
        val accessToken: String,

        @Column(nullable = false, unique = false)
        val size: Long
)