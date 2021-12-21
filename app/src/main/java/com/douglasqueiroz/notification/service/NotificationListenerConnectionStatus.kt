package com.douglasqueiroz.notification.service

sealed class NotificationListenerConnectionStatus {
    class Connected(val notificationListener: NotificationListener): NotificationListenerConnectionStatus()
    object Disconnected: NotificationListenerConnectionStatus()
}
