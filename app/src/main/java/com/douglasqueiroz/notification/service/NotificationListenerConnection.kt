package com.douglasqueiroz.notification.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.douglasqueiroz.notification.dto.NotificationDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationListenerConnection: ServiceConnection {

    private var notificationListener: NotificationListener? = null
    private val _stateFlow = MutableStateFlow<List<NotificationDto>>(
        value = emptyList()
    )
    val stateFlow = _stateFlow.asStateFlow()

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        (service as? NotificationBinder)?.notificationListener?.let { notificationListener ->
            this.notificationListener = notificationListener
            this.notificationListener?.stateFlow = _stateFlow
        }
    }

    fun removeCallbacks() {
        notificationListener?.stateFlow = null
        notificationListener = null
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        removeCallbacks()
    }
}