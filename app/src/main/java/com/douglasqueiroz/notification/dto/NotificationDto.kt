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
        title = statusBarNotification.notification.extras.getString(Notification.EXTRA_TITLE) ?: "No Title",
        content = statusBarNotification.notification.extras.getString(Notification.EXTRA_TEXT) ?: "No Content",
        icon = statusBarNotification.notification.smallIcon,
    )
}
