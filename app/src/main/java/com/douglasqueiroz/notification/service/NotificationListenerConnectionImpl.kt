package com.douglasqueiroz.notification.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class NotificationListenerConnectionImpl: ServiceConnection, NotificationListenerConnection {

    private var notificationListener: NotificationListener? = null
    override fun getNotificationListener() = notificationListener

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        notificationListener = (service as? NotificationBinder)?.notificationListener
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        notificationListener = null
    }
}