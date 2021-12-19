package com.douglasqueiroz.notification.service

import androidx.lifecycle.LiveData
import com.douglasqueiroz.notification.dto.NotificationDto

interface NotificationListener {

    fun getActiveNotificationLiveData(): LiveData<List<NotificationDto>>
}