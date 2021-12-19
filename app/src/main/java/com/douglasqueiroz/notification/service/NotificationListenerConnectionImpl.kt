package com.douglasqueiroz.notification.service

import android.content.ComponentName
import android.os.IBinder

class NotificationListenerConnectionImpl: NotificationListenerConnection {

    private var notificationListener: NotificationListener? = null
    override fun getNotificationListener() = notificationListener

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        notificationListener = (service as? NotificationBinder)?.notificationListener
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        notificationListener = null
    }
}