package com.douglasqueiroz.notification.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationListenerConnection: ServiceConnection {

    private val _stateFlow = MutableStateFlow<NotificationListenerConnectionStatus>(
        value = NotificationListenerConnectionStatus.Disconnected
    )
    val stateFlow = _stateFlow.asStateFlow()

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        (service as? NotificationBinder)?.notificationListener?.let { notificationListener ->
            _stateFlow.value = NotificationListenerConnectionStatus.Connected(notificationListener)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        _stateFlow.value = NotificationListenerConnectionStatus.Disconnected
    }
}