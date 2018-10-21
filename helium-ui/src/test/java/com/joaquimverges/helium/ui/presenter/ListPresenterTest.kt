package com.joaquimverges.helium.ui.presenter


import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.ui.repository.BaseRepository
import com.joaquimverges.helium.ui.state.ListViewState
import com.joaquimverges.helium.ui.util.RefreshPolicy
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ListPresenterTest {

    class TestItem

    @Mock lateinit var refreshPolicy: RefreshPolicy
    @Mock lateinit var repo: BaseRepository<List<TestItem>>

    private lateinit var presenter: ListPresenter<TestItem, ViewEvent>
    private val testScheduler = TestScheduler()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        RxJavaPlugins.setIoSchedulerHandler { _ -> testScheduler }
        RxAndroidPlugins.setMainThreadSchedulerHandler { _ -> testScheduler }
        presenter = spy(ListPresenter(repo, refreshPolicy))
        whenever(repo.getData()).thenReturn(Observable.range(0, 20).map { TestItem() }.toList())
    }

    @Test
    fun testRefreshPolicy() {
        whenever(refreshPolicy.shouldRefresh()).thenReturn(false)
        presenter.refreshIfNeeded()
        verify(presenter, never()).loadData()

        whenever(refreshPolicy.shouldRefresh()).thenReturn(true)
        presenter.refreshIfNeeded()
        verify(presenter).loadData()
    }

    @Test
    fun testRefreshWithData() {
        presenter.loadData()
        verify(presenter).pushState(any<ListViewState.Loading<List<TestItem>>>())
        testScheduler.triggerActions()
        verify(presenter).pushState(any<ListViewState.DataReady<List<TestItem>>>())
        verify(refreshPolicy).updateLastRefreshedTime()
    }

    @Test
    fun testRefreshWithNoData() {
        whenever(repo.getData()).thenReturn(Single.just(emptyList()))
        presenter.loadData()
        verify(presenter).pushState(any<ListViewState.Loading<List<TestItem>>>())
        testScheduler.triggerActions()
        verify(presenter).pushState(any<ListViewState.Empty<List<TestItem>>>())
        verify(refreshPolicy).updateLastRefreshedTime()
    }

    @Test
    fun testRefreshError() {
        whenever(repo.getData()).thenReturn(Single.error<List<TestItem>>(RuntimeException("error")))
        presenter.loadData()
        verify(presenter).pushState(any<ListViewState.Loading<List<TestItem>>>())
        testScheduler.triggerActions()
        verify(presenter).pushState(any<ListViewState.Error<List<TestItem>>>())
        verify(refreshPolicy, never()).updateLastRefreshedTime()
    }
}
