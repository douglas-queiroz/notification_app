package com.douglasqueiroz.notification.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.douglasqueiroz.notification.dto.NotificationDto
import com.douglasqueiroz.notification.repository.NotificationDao
import com.douglasqueiroz.notification.rule.MainCoroutineScopeRule
import com.douglasqueiroz.notification.service.NotificationListenerConnection
import com.douglasqueiroz.notification.testObserver
import com.douglasqueiroz.notification.util.IconUtil
import com.douglasqueiroz.notification.util.PermissionUtil
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = MainCoroutineScopeRule()

    @MockK
    private lateinit var connection: NotificationListenerConnection

    @MockK
    private lateinit var permissionUtil: PermissionUtil

    @MockK
    private lateinit var notificationDao: NotificationDao

    @MockK
    private lateinit var iconUtil: IconUtil

    private lateinit var target: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        target = MainViewModel(
            connection= connection,
            permissionUtil = permissionUtil,
            notificationDao = notificationDao,
            iconUtil= iconUtil
        )
    }

    @Test
    fun `GIVEN getStateLiveData then bind service`() {
        val liveState = target.getStateLiveData().testObserver()
        assertEquals(MainViewModel.State.BindService(connection), liveState.observedValues.last())
    }

    @Test
    fun `GIVEN checkPermission when permission is not granted then set permission button visible`() {
        every {
            permissionUtil.notificationPermissionGrant()
        }.returns(false)

        val liveState = target.getStateLiveData().testObserver()
        target.checkNotificationPermission()

        assertEquals(MainViewModel.State.SetPermissionButtonVisible, liveState.observedValues.last())
    }

    @Test
    fun `GIVEN checkPermission when permission is granted then show empty list view`() {
        every {
            permissionUtil.notificationPermissionGrant()
        }.returns(true)

        val liveState = target.getStateLiveData().testObserver()
        target.checkNotificationPermission()

        assertEquals(MainViewModel.State.ShowEmptyListView, liveState.observedValues.last())
    }

    @Test
    fun `GIVEN getStateLiveData when tracked notifications is empty then show empty screen`()  {

        val flow = flowOf(emptyList<NotificationDto>())
        every { notificationDao.getAll() } returns flow
        every { permissionUtil.notificationPermissionGrant() }.returns(true)

        val liveState = target.getStateLiveData().testObserver()

        assertEquals(
            MainViewModel.State.ShowEmptyListView,
            liveState.observedValues.last()
        )
    }

    @Test
    fun `GIVEN getStateLiveData when tracked notifications is updated then update notification list`()  {

        val title = "title"
        val content = "content"
        val packageName = "content"
        val expected = listOf(NotificationItem(null, title, content))

        val flow = flowOf(listOf(NotificationDto(1, title, content, packageName)))
        every { notificationDao.getAll() } returns flow
        every { permissionUtil.notificationPermissionGrant() }.returns(true)
        every { iconUtil.getIcon(packageName) }.returns(null)

        val liveState = target.getStateLiveData().testObserver()

        assertEquals(
            MainViewModel.State.UpdateNotificationList(expected),
            liveState.observedValues.last()
        )
    }

    @Test
    fun `GIVEN getStateLiveData when select active notification and it has no notification then show empty scree`()  {

        val flow = MutableStateFlow(emptyList<NotificationDto>())
        every { connection.stateFlow } returns flow
        every { permissionUtil.notificationPermissionGrant() } returns true

        val liveState = target.getStateLiveData().testObserver()
        target.setNotificationSource(NotificationSource.ACTIVE_NOTIFICATIONS)

        assertEquals(
            MainViewModel.State.ShowEmptyListView,
            liveState.observedValues.last()
        )
    }

    @Test
    fun `GIVEN getStateLiveData when select active notification then show notification list loaded the service`()  {

        val title = "title"
        val content = "content"
        val packageName = "content"
        val expected = listOf(NotificationItem(null, title, content))

        val flow = MutableStateFlow(listOf(NotificationDto(1, title, content, packageName)))
        every { connection.stateFlow } returns flow
        every { permissionUtil.notificationPermissionGrant() } returns true
        every { iconUtil.getIcon(packageName) } returns null

        val liveState = target.getStateLiveData().testObserver()
        target.setNotificationSource(NotificationSource.ACTIVE_NOTIFICATIONS)

        assertEquals(
            MainViewModel.State.UpdateNotificationList(expected),
            liveState.observedValues.last()
        )
    }
}