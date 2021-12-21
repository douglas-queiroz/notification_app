package com.douglasqueiroz.notification.service

sealed class NotificationListenerEvent {
    object Connected: NotificationListenerEvent()
    object Disconnected: NotificationListenerEvent()
    object NewNotification: NotificationListenerEvent()
}