package com.douglasqueiroz.notification.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.douglasqueiroz.notification.databinding.ActivityMainBinding
import com.douglasqueiroz.notification.dto.NotificationDto
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



        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        moviesAdapter = NotificationAdapter()
        binding.notificationRecyclerView.adapter = moviesAdapter
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launchWhenCreated {
            viewModel.stateFlow.collectLatest { state ->
                when(state) {
                    is MainViewModel.State.BindService -> bindService(state.connection)
                    is MainViewModel.State.UnbindService -> unbindService(state.connection)
                    is MainViewModel.State.UpdateNotificationList -> updateNotificationList(state.notificationList)
                }
            }
        }
    }

    private fun bindService(connection: NotificationListenerConnection) {
        Intent(this, NotificationListener::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun updateNotificationList(notificationList: List<NotificationDto>) {
        notificationList.forEach {
            Log.i("RiM", "${it.title}")
        }

        moviesAdapter.notificationList = notificationList
        moviesAdapter.notifyDataSetChanged()
    }

    private fun unbindService(connection: NotificationListenerConnection) {
        super.unbindService(connection)
    }
}