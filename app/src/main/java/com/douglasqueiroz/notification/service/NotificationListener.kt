package com.douglasqueiroz.notification.service

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.douglasqueiroz.notification.dto.NotificationDto
import com.douglasqueiroz.notification.repository.NotificationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import leakcanary.AppWatcher
import org.koin.android.ext.android.inject

open class NotificationListener: NotificationListenerService() {

    private val notificationDao: NotificationDao by inject()
    private val ioScope = CoroutineScope(Dispatchers.IO + Job())
    private var isConnected = false

    var stateFlow: MutableStateFlow<List<NotificationDto>>? = null
    set(value) {
        field = value
        updateActiveNotifications()
    }

    override fun onBind(intent: Intent?) = when(intent?.action) {
            SERVICE_INTERFACE -> super.onBind(intent)
            else -> NotificationBinder(this)
        }

    override fun onListenerConnected() {
        super.onListenerConnected()
        isConnected = true
        updateActiveNotifications()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        saveNotification(sbn)
        updateActiveNotifications()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        updateActiveNotifications()
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        isConnected = false
    }

    private fun updateActiveNotifications() {
        if (isConnected) {
            activeNotifications.map {
                NotificationDto(it)
            }.also {
                stateFlow?.value = it
            }
        }
    }

    private fun saveNotification(sbn: StatusBarNotification?) = ioScope.launch {
        sbn?.let { statusBarNotification ->
            NotificationDto(statusBarNotification)
        }?.also { notification ->
            notificationDao.insert(notification)
        }
    }
}