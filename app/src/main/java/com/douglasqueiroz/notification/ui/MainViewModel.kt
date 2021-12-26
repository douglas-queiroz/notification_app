package com.douglasqueiroz.notification.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.douglasqueiroz.notification.dto.NotificationDto
import com.douglasqueiroz.notification.repository.NotificationDao
import com.douglasqueiroz.notification.service.NotificationListenerConnection
import com.douglasqueiroz.notification.util.IconUtil
import com.douglasqueiroz.notification.util.PermissionUtil
import kotlinx.coroutines.flow.*

class MainViewModel(
    private val connection: NotificationListenerConnection,
    private val permissionUtil: PermissionUtil,
    private val iconUtil: IconUtil,
    notificationDao: NotificationDao
): ViewModel() {

    private var notificationSource = NotificationSource.TRACKED_NOTIFICATIONS

    private var trackedNotificationList = emptyList<NotificationDto>()
    private var activeNotificationList = emptyList<NotificationDto>()

    private var source = MutableStateFlow<State>(State.BindService(connection))
    private val notificationDaoFlow by lazy {
        notificationDao.getAll()
            .map {
                trackedNotificationList = it
                it
            }.filter {
                notificationSource == NotificationSource.TRACKED_NOTIFICATIONS
                        && permissionUtil.notificationPermissionGrant()
            }.map {
                updateNotificationList(it)
            }
    }

    private val serviceFlow by lazy {
        connection.stateFlow
            .map {
                activeNotificationList = it
                it
            }.filter {
                notificationSource == NotificationSource.ACTIVE_NOTIFICATIONS
            }.map {
                updateNotificationList(it)
            }
    }

    fun getStateLiveData() = flowOf(
            source,
            notificationDaoFlow,
            serviceFlow
        ).flattenMerge().asLiveData(viewModelScope.coroutineContext)

    sealed class State {
        object SetPermissionButtonVisible: State()
        data class UpdateNotificationList(val notificationList: List<NotificationItem>): State()
        data class BindService(val connection: NotificationListenerConnection): State()
        data class UnbindService(val connection: NotificationListenerConnection): State()
        object ShowEmptyListView: State()
    }

    fun checkNotificationPermission() {
        if(!permissionUtil.notificationPermissionGrant()) {
            source.value = State.SetPermissionButtonVisible
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

        source.value = updateNotificationList(list)
    }

    private fun updateNotificationList(notificationDtoList: List<NotificationDto>): State {
        return notificationDtoList.map {
            NotificationItem(
                icon = iconUtil.getIcon(it.packageName),
                title = it.title,
                content = it.content
            )
        }.let {
            if (it.isEmpty()) {
                State.ShowEmptyListView
            } else {
                State.UpdateNotificationList(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        connection.removeCallbacks()
        source.value = State.UnbindService(connection)
    }
}