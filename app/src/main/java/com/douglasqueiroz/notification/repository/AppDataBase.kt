package com.douglasqueiroz.notification.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.douglasqueiroz.notification.dto.NotificationDto

@Database(entities = [NotificationDto::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}