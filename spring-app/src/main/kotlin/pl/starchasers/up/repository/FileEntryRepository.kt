package pl.starchasers.up.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import pl.starchasers.up.data.model.FileEntry

interface FileEntryRepository : JpaRepository<FileEntry, Long> {

    @Query("""
        from FileEntry f where f.key=:key 
    """)
    fun findExistingFileByKey(key: String): FileEntry?

    @Query("""
        from FileEntry f
        where f.permanent = false
            and f.toDeleteDate < current_timestamp
    """)
    fun findExpiredFiles(): Set<FileEntry>
}
