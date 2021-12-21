package com.douglasqueiroz.notification

import android.app.Application
import com.douglasqueiroz.notification.di.AppModule

class NotificationApp: Application() {

    override fun onCreate() {
        super.onCreate()
        AppModule.startKoin(this)
    }
}