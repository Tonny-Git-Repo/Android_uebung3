package de.thm.ap.records.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import de.thm.ap.records.model.Record

@Dao
interface RecordDAO {

    @Update(onConflict = REPLACE)
    fun update(record: Record): Int

    @Insert(onConflict = IGNORE)
    fun persist(record: Record): Long

    @Delete
    fun delete(record: Record): Int

    @Delete
    fun deleteAll(records: List<Record>)


    @Query("SELECT * FROM record")
    fun findAll(): List<Record>

    @Query("SELECT * FROM record")
    fun findAllSync(): LiveData<List<Record>>

    @Query("SELECT * FROM record WHERE id = :id")
    fun findByIdSync(id: Int): Record?

}