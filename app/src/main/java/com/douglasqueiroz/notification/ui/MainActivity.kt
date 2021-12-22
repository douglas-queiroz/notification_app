package com.douglasqueiroz.notification.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.douglasqueiroz.notification.R
import com.douglasqueiroz.notification.databinding.ActivityMainBinding
import com.douglasqueiroz.notification.service.NotificationListener
import com.douglasqueiroz.notification.service.NotificationListenerConnection
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var moviesAdapter: NotificationAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.permissionButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        }

        setupRecyclerView()
        subscribeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkNotificationPermission()
    }

    private fun setupRecyclerView() {
        moviesAdapter = NotificationAdapter()
        binding.notificationRecyclerView.adapter = moviesAdapter
    }

    private fun subscribeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest { state ->
                when (state) {
                    is MainViewModel.State.UpdateNotificationList -> updateNotificationList(state.notificationList)
                    is MainViewModel.State.SetPermissionButtonVisible -> setPermissionButtonVisible()
                    is MainViewModel.State.ShowEmptyListView -> showEmptyListView()
                    is MainViewModel.State.BindService -> bindService(state.connection)
                    is MainViewModel.State.UnbindService -> unbindService(state.connection)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_notification_source, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_active_notifications -> NotificationSource.ACTIVE_NOTIFICATIONS
            R.id.action_tracked_notifications -> NotificationSource.TRACKED_NOTIFICATIONS
            else -> null
        }?.also {
            viewModel.setNotificationSource(it)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun bindService(connection: NotificationListenerConnection) {
        Intent(this, NotificationListener::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun updateNotificationList(notificationList: List<NotificationItem>) {
        moviesAdapter.notificationList = notificationList
        moviesAdapter.notifyDataSetChanged()

        binding.notificationRecyclerView.isVisible = true
        binding.emptyMsgTextView.isVisible = false
        binding.permissionButton.isVisible = false
    }

    private fun unbindService(connection: NotificationListenerConnection) {
        super.unbindService(connection)
    }

    private fun setPermissionButtonVisible() {
        binding.permissionButton.isVisible = true
        binding.notificationRecyclerView.isVisible = false
        binding.emptyMsgTextView.isVisible = false
    }

    private fun showEmptyListView() {
        binding.emptyMsgTextView.isVisible = true
        binding.permissionButton.isVisible = false
        binding.notificationRecyclerView.isVisible = false
    }
}