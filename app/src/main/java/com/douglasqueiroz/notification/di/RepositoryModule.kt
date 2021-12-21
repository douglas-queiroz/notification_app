package com.douglasqueiroz.notification.di

import android.app.Application
import androidx.room.Room
import com.douglasqueiroz.notification.repository.AppDataBase
import com.douglasqueiroz.notification.repository.NotificationDao
import org.koin.dsl.module

object RepositoryModule {

    private const val DB_NAME = "notification-app"

    fun get() = module {

        fun provideDataBase(application: Application): AppDataBase {
            return Room.databaseBuilder(application, AppDataBase::class.java, DB_NAME).build()
        }

        fun provideNotificationDao(dataBase: AppDataBase): NotificationDao {
            return dataBase.notificationDao()
        }

        single { provideDataBase(get())}
        single { provideNotificationDao(get()) }
    }
}