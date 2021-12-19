package com.douglasqueiroz.notification.di

import com.douglasqueiroz.notification.service.NotificationListenerConnection
import com.douglasqueiroz.notification.service.NotificationListenerConnectionImpl
import org.koin.dsl.module

object ServiceModule {

    fun get() = module {
        single<NotificationListenerConnection> { NotificationListenerConnectionImpl() }
    }
}
