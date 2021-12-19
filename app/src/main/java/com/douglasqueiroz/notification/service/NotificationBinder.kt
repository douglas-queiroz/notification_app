package com.douglasqueiroz.notification.service

import android.os.Binder
import com.douglasqueiroz.notification.service.NotificationService

class NotificationBinder(
    val service: NotificationService
): Binder()
