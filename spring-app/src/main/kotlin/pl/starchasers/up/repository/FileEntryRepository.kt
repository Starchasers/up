package pl.starchasers.up.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.starchasers.up.data.model.FileEntry

interface FileEntryRepository : JpaRepository<FileEntry, Long> {

}
