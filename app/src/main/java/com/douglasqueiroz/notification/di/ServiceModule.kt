package com.douglasqueiroz.notification.di

import com.douglasqueiroz.notification.service.NotificationListenerConnection
import org.koin.dsl.module

object ServiceModule {

    fun get() = module {
        single { NotificationListenerConnection() }
    }
}
