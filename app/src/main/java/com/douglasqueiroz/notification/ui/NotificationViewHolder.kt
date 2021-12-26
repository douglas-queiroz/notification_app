package com.douglasqueiroz.notification.ui

import androidx.recyclerview.widget.RecyclerView
import com.douglasqueiroz.notification.databinding.NotificationItemBinding

class NotificationViewHolder(
    private val binding: NotificationItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun setup(notification: NotificationItem) = with(binding) {
        notificationIconImageView.setImageDrawable(notification.icon)
        titleTextView.text = notification.title
        contentTextView.text = notification.content
    }
}