package com.douglasqueiroz.notification.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasqueiroz.notification.dto.NotificationDto
import com.douglasqueiroz.notification.repository.NotificationDao
import com.douglasqueiroz.notification.service.NotificationListenerConnection
import com.douglasqueiroz.notification.util.IconUtil
import com.douglasqueiroz.notification.util.PermissionUtil
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val connection: NotificationListenerConnection,
    private val permissionUtil: PermissionUtil,
    private val notificationDao: NotificationDao,
    private val iconUtil: IconUtil
): ViewModel() {

    private var notificationSource = NotificationSource.TRACKED_NOTIFICATIONS

    private var trackedNotificationList = emptyList<NotificationDto>()
    private var activeNotificationList = emptyList<NotificationDto>()

    private var _stateFlow = MutableStateFlow<State>(State.BindService(connection))
    val stateFlow = _stateFlow.asStateFlow()

    sealed class State {
        object SetPermissionButtonVisible: State()
        class UpdateNotificationList(val notificationList: List<NotificationItem>): State()
        class BindService(val connection: NotificationListenerConnection): State()
        class UnbindService(val connection: NotificationListenerConnection): State()
        object ShowEmptyListView: State()
    }

    init {
        collectServiceConnectionStatus()
        collectNotificationDao()
    }

    private fun collectNotificationDao() = viewModelScope.launch {
        notificationDao.getAll().collectLatest {
            trackedNotificationList = it

            if (notificationSource == NotificationSource.TRACKED_NOTIFICATIONS
                && permissionUtil.notificationPermissionGrant()) {
                updateNotificationList(it)
            }
        }
    }

    fun checkNotificationPermission() {
        if(!permissionUtil.notificationPermissionGrant()) {
            _stateFlow.value = State.SetPermissionButtonVisible
        } else {
            updateListFromSource(notificationSource)
        }
    }

    fun setNotificationSource(newSource: NotificationSource) {
        if (newSource != notificationSource && permissionUtil.notificationPermissionGrant()) {
            notificationSource = newSource
            updateListFromSource(newSource)
        }
    }

    private fun updateListFromSource(newSource: NotificationSource) {
        val list = when (newSource) {
            NotificationSource.TRACKED_NOTIFICATIONS -> trackedNotificationList
            NotificationSource.ACTIVE_NOTIFICATIONS -> activeNotificationList
        }

        updateNotificationList(list)
    }

    private fun collectServiceConnectionStatus() = viewModelScope.launch {
        connection.stateFlow.collectLatest { list ->
            this@MainViewModel.activeNotificationList = list
            if (notificationSource == NotificationSource.ACTIVE_NOTIFICATIONS) {
                updateNotificationList(list)
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
            if (it.isEmpty()) {
                _stateFlow.value = State.ShowEmptyListView
            } else {
                _stateFlow.value = State.UpdateNotificationList(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        connection.removeCallbacks()
        _stateFlow.value = State.UnbindService(connection)
    }
}