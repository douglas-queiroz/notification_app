package com.douglasqueiroz.notification.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.douglasqueiroz.notification.dto.NotificationDto
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM NotificationDto")
    fun getAll(): Flow<List<NotificationDto>>

    @Insert
    fun insert(notificationDto: NotificationDto)
}