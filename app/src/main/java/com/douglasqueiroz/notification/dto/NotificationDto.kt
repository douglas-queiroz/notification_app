package com.douglasqueiroz.notification.dto

import android.app.Notification
import android.service.notification.StatusBarNotification
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotificationDto(
    @PrimaryKey
    val id: Int,
    val title: String,
    val content: String,
    val packageName: String
) {

    constructor(sbn: StatusBarNotification): this(
        id = sbn.id,
        title = sbn.notification.extras.get(Notification.EXTRA_TITLE) as? String ?: "No Title",
        content = sbn.notification.extras.get(Notification.EXTRA_TEXT) as? String ?: "No Content",
        packageName = sbn.packageName
    )
}
