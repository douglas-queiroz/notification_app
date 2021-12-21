package com.douglasqueiroz.notification.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.douglasqueiroz.notification.databinding.NotificationItemBinding
import com.douglasqueiroz.notification.dto.NotificationDto

class NotificationAdapter: RecyclerView.Adapter<NotificationViewHolder>() {

    var notificationList = emptyList<NotificationDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NotificationItemBinding.inflate(inflater, parent, false)

        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.setup(notification)
    }

    override fun getItemCount() = notificationList.count()
}