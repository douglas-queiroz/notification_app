package com.douglasqueiroz.notification.service

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.douglasqueiroz.notification.dto.NotificationDto
import com.douglasqueiroz.notification.repository.NotificationDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.ext.android.inject

class NotificationListener: NotificationListenerService() {

    private val notificationDao: NotificationDao by inject()

    private val _stateFlow = MutableStateFlow<NotificationListenerEvent>(
        value = NotificationListenerEvent.Disconnected
    )
    val stateFlow = _stateFlow.asStateFlow()

    fun getActiveNotification() = activeNotifications.map { NotificationDto(it) }

    override fun onBind(intent: Intent?) = when(intent?.action) {
            SERVICE_INTERFACE -> super.onBind(intent)
            else -> NotificationBinder(this)
        }

    override fun onListenerConnected() {
        super.onListenerConnected()
        _stateFlow.value = NotificationListenerEvent.Connected
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        _stateFlow.value = NotificationListenerEvent.NewNotification
        saveNotification(sbn)
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        _stateFlow.value = NotificationListenerEvent.Disconnected
    }

    private fun saveNotification(sbn: StatusBarNotification?) {
        sbn?.let { statusBarNotification ->
            NotificationDto(statusBarNotification)
        }?.also { notification ->
            notificationDao.insert(notification)
        }
    }
}