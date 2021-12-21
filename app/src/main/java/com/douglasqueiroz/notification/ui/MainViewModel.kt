package com.douglasqueiroz.notification.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasqueiroz.notification.dto.NotificationDto
import com.douglasqueiroz.notification.service.NotificationListener
import com.douglasqueiroz.notification.service.NotificationListenerConnection
import com.douglasqueiroz.notification.service.NotificationListenerConnectionStatus
import com.douglasqueiroz.notification.service.NotificationListenerEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val connection: NotificationListenerConnection
): ViewModel() {

    private var _stateFlow = MutableStateFlow<State>(State.BindService(connection))
    var stateFlow = _stateFlow.asStateFlow()

    sealed class State {
        class UpdateNotificationList(val notificationList: List<NotificationDto>): State()
        class BindService(val connection: NotificationListenerConnection): State()
        class UnbindService(val connection: NotificationListenerConnection): State()
    }

    init {
        loadNotifications()
    }

    private fun loadNotifications() = viewModelScope.launch {

        connection.stateFlow.collectLatest { status ->
            when(status) {
                is NotificationListenerConnectionStatus.Connected -> {
                    handleStatusConnected(status.notificationListener)
                }
                is NotificationListenerConnectionStatus.Disconnected -> {}
            }
        }
    }

    private suspend fun handleStatusConnected(notificationListener: NotificationListener) {
        notificationListener.stateFlow.collectLatest { event ->
            when(event) {
                is NotificationListenerEvent.Connected, NotificationListenerEvent.NewNotification -> {
                    _stateFlow.value = State.UpdateNotificationList(notificationListener.getActiveNotification())
                }
                is NotificationListenerEvent.Disconnected -> {}
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _stateFlow.value = State.UnbindService(connection)
    }
}