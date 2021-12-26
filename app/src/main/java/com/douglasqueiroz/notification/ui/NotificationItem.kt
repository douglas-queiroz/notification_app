package com.douglasqueiroz.notification.ui

import android.graphics.drawable.Drawable

data class NotificationItem(
    val icon: Drawable?,
    val title: String,
    val content: String
)