package com.douglasqueiroz.notification.service

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.MutableLiveData

class NotificationService: NotificationListenerService() {

    private var livedata = MutableLiveData<Array<StatusBarNotification>>()

    override fun onBind(intent: Intent?) = when(intent?.action) {
            SERVICE_INTERFACE -> super.onBind(intent)
            else -> NotificationBinder(this)
        }

    override fun onListenerConnected() {
        super.onListenerConnected()
        livedata.postValue(activeNotifications)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        livedata.postValue(activeNotifications)
    }
}