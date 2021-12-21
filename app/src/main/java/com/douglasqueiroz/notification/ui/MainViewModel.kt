package com.douglasqueiroz.notification.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasqueiroz.notification.dto.NotificationDto
import com.douglasqueiroz.notification.repository.NotificationDao
import com.douglasqueiroz.notification.service.NotificationListener
import com.douglasqueiroz.notification.service.NotificationListenerConnection
import com.douglasqueiroz.notification.service.NotificationListenerConnectionStatus
import com.douglasqueiroz.notification.service.NotificationListenerEvent
import com.douglasqueiroz.notification.util.IconUtil
import com.douglasqueiroz.notification.util.PermissionUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val connection: NotificationListenerConnection,
    private val permissionUtil: PermissionUtil,
    private val notificationDao: NotificationDao,
    private val iconUtil: IconUtil
): ViewModel() {

    private var notificationSource = NotificationSource.ACTIVE_NOTIFICATIONS

    private var _stateFlow = MutableStateFlow<State>(State.BindService(connection))
    var stateFlow = _stateFlow.asStateFlow()

    sealed class State {
        class SetPermissionButtonVisible(val visible: Boolean): State()
        class UpdateNotificationList(val notificationList: List<NotificationItem>): State()
        class BindService(val connection: NotificationListenerConnection): State()
        class UnbindService(val connection: NotificationListenerConnection): State()
    }

    init {
        loadNotifications()
    }

    fun checkNotificationPermission() {
        if(!permissionUtil.notificationPermissionGrant()) {
            _stateFlow.value = State.SetPermissionButtonVisible(true)
        }
    }

    fun changeNotificationSource(newSource: NotificationSource) {
        when(newSource) {
            NotificationSource.ACTIVE_NOTIFICATIONS -> handleActiveNotificationSource()
            NotificationSource.TRACKED_NOTIFICATIONS -> handleTrackedNotificationSource()
        }
    }

    private fun handleActiveNotificationSource() {
        _stateFlow.value = State.BindService(connection)
    }

    private fun handleTrackedNotificationSource() {
        _stateFlow.value = State.UnbindService(connection)

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
                    updateNotificationList(notificationListener.getActiveNotification())
                }
                is NotificationListenerEvent.Disconnected -> {}
            }
        }
    }

    private fun updateNotificationList(notificationDtoList: List<NotificationDto>) {
        notificationDtoList.map {
            NotificationItem(
                icon = iconUtil.getIcon(it.packageName),
                title = it.title,
                content = it.content
            )
        }.also {
            _stateFlow.value = State.UpdateNotificationList(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _stateFlow.value = State.UnbindService(connection)
    }
}