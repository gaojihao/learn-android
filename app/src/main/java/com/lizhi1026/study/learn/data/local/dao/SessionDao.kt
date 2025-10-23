package com.lizhi1026.study.learn.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lizhi1026.study.learn.data.local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions WHERE startTime >= :since ORDER BY startTime DESC")
    fun observeSince(since: Long): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE id = :id LIMIT 1")
    suspend fun getById(id: UUID): SessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(session: SessionEntity)

    @Query("DELETE FROM sessions WHERE startTime < :threshold")
    suspend fun deleteOlderThan(threshold: Long)
}