package com.douglasqueiroz.notification.service

import android.os.Binder

class NotificationBinder(
    val notificationListener: NotificationListenerImpl
): Binder()
