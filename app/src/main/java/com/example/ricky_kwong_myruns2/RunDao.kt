package com.example.ricky_kwong_myruns2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {
    @Insert
    suspend fun insertRun(run: Run)

    @Query("SELECT * FROM run_table")
    fun getRunData(): Flow<List<Run>>

    @Query("DELETE FROM run_table WHERE id = :key")
    suspend fun deleteRun(key: Long)

    @Query("DELETE FROM run_table")
    suspend fun deleteAllEntries()
}