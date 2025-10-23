/**
 * 文件：TaskDao.kt
 * 简介：Room DAO
 * 关键职责：访问任务数据的增删改查接口
 * 相关组件：AppDatabase、TaskEntity、Repositories
 */
package com.lizhi1026.study.learn.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lizhi1026.study.learn.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    /**
     * 观察所有任务（按创建时间倒序）
     * 返回 Room Flow 实体列表
     */
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<TaskEntity>>

    /** 插入或更新任务 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(task: TaskEntity)

    /** 更新任务（局部字段变更） */
    @Update
    suspend fun update(task: TaskEntity)

    /** 删除任务 */
    @Delete
    suspend fun delete(task: TaskEntity)

    /** 按主键查询任务，未找到返回 null */
    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getById(id: java.util.UUID): TaskEntity?
}