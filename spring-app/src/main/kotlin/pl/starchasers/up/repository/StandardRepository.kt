package pl.starchasers.up.repository

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.Table
import org.ktorm.support.postgresql.BulkInsertStatementBuilder
import org.ktorm.support.postgresql.bulkInsert
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class StandardRepository<T : Table<*>>(
    internal open val table: T,
    open val database: Database
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun update(record: UpdateStatementBuilder.(T) -> Unit): Int =
        database.update(table, record)

    fun insert(record: AssignmentsBuilder.(T) -> Unit): Int =
        database.insert(table, record)

    fun bulkInsert(records: BulkInsertStatementBuilder<T>.(T) -> Unit) =
        database.bulkInsert(table, records)

    fun deleteAll(): Int {
        logger.warn("Deleting everything from ${table.tableName}")
        return database.deleteAll(table)
    }

    fun count(): Int = table.primaryKeys.firstOrNull()?.let { pk ->
        database.from(table).select(count(pk)).map { it.getInt(1) }.first()
    } ?: throw IllegalStateException("Table ${table.tableName} has no primary key")

}

