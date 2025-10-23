/**
 * 文件：SessionDao.kt
 * 简介：Room DAO
 * 关键职责：访问会话数据的增删改查接口
 * 相关组件：AppDatabase、SessionEntity、Repositories
 */
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
    /**
     * 观察指定起始时间以来的会话列表（按开始时间倒序）
     * @param since 起始时间（毫秒）
     */
    @Query("SELECT * FROM sessions WHERE startTime >= :since ORDER BY startTime DESC")
    fun observeSince(since: Long): Flow<List<SessionEntity>>

    /** 按主键查询会话，未找到返回 null */
    @Query("SELECT * FROM sessions WHERE id = :id LIMIT 1")
    suspend fun getById(id: UUID): SessionEntity?

    /** 插入或更新会话 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(session: SessionEntity)

    /** 删除指定阈值之前的旧会话 */
    @Query("DELETE FROM sessions WHERE startTime < :threshold")
    suspend fun deleteOlderThan(threshold: Long)
}