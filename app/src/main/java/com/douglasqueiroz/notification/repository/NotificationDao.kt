package com.douglasqueiroz.notification.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.douglasqueiroz.notification.dto.NotificationDto
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM NotificationDto order by id limit 20")
    fun getAll(): Flow<List<NotificationDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notificationDto: NotificationDto)
}