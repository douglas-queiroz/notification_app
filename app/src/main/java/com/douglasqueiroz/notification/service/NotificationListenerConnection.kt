package com.douglasqueiroz.notification.service

import android.content.ServiceConnection

interface NotificationListenerConnection: ServiceConnection {

    fun getNotificationListener(): NotificationListener?
}