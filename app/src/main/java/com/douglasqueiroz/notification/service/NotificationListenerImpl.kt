package com.douglasqueiroz.notification.service

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.MutableLiveData
import com.douglasqueiroz.notification.dto.NotificationDto

class NotificationListenerImpl: NotificationListenerService(), NotificationListener {

    private var livedata = MutableLiveData<List<NotificationDto>>()

    override fun getActiveNotificationLiveData() = livedata

    override fun onBind(intent: Intent?) = when(intent?.action) {
            SERVICE_INTERFACE -> super.onBind(intent)
            else -> NotificationBinder(this)
        }

    override fun onListenerConnected() {
        super.onListenerConnected()
        updateActiveNotificationLiveData()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        updateActiveNotificationLiveData()
    }

    private fun updateActiveNotificationLiveData() {
        val notifications = activeNotifications.map { NotificationDto(it) }
        livedata.postValue(notifications)
    }
}