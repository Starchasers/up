package pl.starchasers.up.repository

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import pl.starchasers.up.data.model.*
import java.time.Instant

@Service
class FileEntryRepository(
    database: Database
) : StandardRepository<FileEntries>(FileEntries, database) {

    fun findExistingFileByKey(key: FileKey): FileEntry? =
        database.from(table)
            .select()
            .where { table.key eq key.value }
            .mapToRecords()
            .firstOrNull()

    fun findExpiredFiles(): Set<FileEntry> =
        database.from(table)
            .select()
            .where { table.deleteAt.isNotNull() and table.deleteAt.less(Instant.now()) }
            .mapToRecords()
            .toSet()

    fun delete(id: FileId) =
        database.delete(table) {
            it.id eq id.value
        }

    fun findAll() =
        database.from(table)
            .select()
            .mapToRecords()

    private fun Query.mapToRecords() =
        this.map {
            FileEntry(
                id = FileId(it[table.id]!!),
                accessToken = it[table.accessToken]?.let(::FileAccessToken),
                contentType = MediaType.valueOf(it[table.contentType]!!),
                createdAt = it[table.createdAt]!!,
                encrypted = it[table.encrypted]!!,
                filename = FileName(it[table.filename]!!),
                key = FileKey(it[table.key]!!),
                password = it[table.password]?.let(::FilePassword),
                size = FileSize(it[table.size]!!),
                toDeleteAt = it[table.deleteAt]
            )
        }
}
