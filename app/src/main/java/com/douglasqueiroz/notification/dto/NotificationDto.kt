package com.douglasqueiroz.notification.dto

import android.app.Notification
import android.graphics.drawable.Icon
import android.service.notification.StatusBarNotification

data class NotificationDto(
    val title: String,
    val content: String,
    val icon: Icon
) {

    constructor(statusBarNotification: StatusBarNotification): this(
        title = statusBarNotification.notification.extras.get(Notification.EXTRA_TITLE) as? String ?: "No Title",
        content = statusBarNotification.notification.extras.get(Notification.EXTRA_TEXT) as? String ?: "No Content",
        icon = statusBarNotification.notification.smallIcon,
    )
}
