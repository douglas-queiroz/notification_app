package com.douglasqueiroz.notification.ui

import androidx.recyclerview.widget.RecyclerView
import com.douglasqueiroz.notification.databinding.NotificationItemBinding
import com.douglasqueiroz.notification.dto.NotificationDto

class NotificationViewHolder(
    private val binding: NotificationItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun setup(notification: NotificationDto) = with(binding) {
        notificationIconImageView.setImageIcon(notification.icon)
        titleTextView.text = notification.title
        contentTextView.text = notification.content
    }
}