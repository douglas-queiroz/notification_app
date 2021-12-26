package com.douglasqueiroz.notification.util

import android.content.Context
import android.provider.Settings

class PermissionUtil(private val context: Context) {

    companion object {
        private const val NOTIFICATION_PERMISSION_NAME = "enabled_notification_listeners"
    }

    fun notificationPermissionGrant() = Settings.Secure.getString(
        context.contentResolver,
        NOTIFICATION_PERMISSION_NAME
    ).contains(context.packageName)

}