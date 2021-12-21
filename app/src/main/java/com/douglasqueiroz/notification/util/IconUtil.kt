package com.douglasqueiroz.notification.util

import android.content.Context
import android.content.pm.PackageManager

class IconUtil(
    private val context: Context
) {

    fun getIcon(packageName: String) = try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
}