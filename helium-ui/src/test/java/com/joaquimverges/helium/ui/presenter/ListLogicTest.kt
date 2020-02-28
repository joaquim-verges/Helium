package com.joaquimverges.helium.ui.presenter

import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.test.HeliumTestCase
import com.joaquimverges.helium.test.viewdelegate.TestUiBlock
import com.joaquimverges.helium.ui.list.ListLogic
import com.joaquimverges.helium.ui.list.event.ListBlockEvent
import com.joaquimverges.helium.ui.list.repository.ListRepository
import com.joaquimverges.helium.ui.list.state.ListBlockState
import com.joaquimverges.helium.ui.util.RefreshPolicy
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ListLogicTest : HeliumTestCase() {

    class TestItem

    @Mock lateinit var refreshPolicy: RefreshPolicy
    @Mock lateinit var repo: ListRepository<List<TestItem>>

    private lateinit var logic: ListLogic<TestItem, BlockEvent>
    private val testScheduler = TestScheduler()
    private val testUi = TestUiBlock<ListBlockState<List<TestItem>>, ListBlockEvent<BlockEvent>>()
    private val testData = Observable.range(0, 20).map { TestItem() }.toList().blockingGet()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxAndroidPlugins.setMainThreadSchedulerHandler { testScheduler }
        whenever(repo.getData()).thenReturn(Single.just(testData))
        logic = ListLogic(repo, refreshPolicy)
        logic.attach(testUi)
    }

    @Test
    fun testRefreshPolicy() {
        whenever(refreshPolicy.shouldRefresh()).thenReturn(false)
        logic.refreshIfNeeded()
        testUi.assertLastRendered(ListBlockState.Init<TestItem>())
        whenever(refreshPolicy.shouldRefresh()).thenReturn(true)
        logic.refreshIfNeeded()
        testUi.assertLastRendered(ListBlockState.Loading<TestItem>())
    }

    @Test
    fun testRefreshWithData() {
        logic.loadFirstPage()
        testUi.assertLastRendered(ListBlockState.Loading<List<TestItem>>())
        testScheduler.triggerActions()
        testUi.assertLastRendered(ListBlockState.DataReady<List<TestItem>>(testData))
        verify(refreshPolicy).updateLastRefreshedTime()
    }

    @Test
    fun testRefreshWithNoData() {
        whenever(repo.getData()).thenReturn(Single.just(emptyList()))
        logic.loadFirstPage()
        testUi.assertLastRendered(ListBlockState.Loading<List<TestItem>>())
        testScheduler.triggerActions()
        testUi.assertLastRendered(ListBlockState.Empty<List<TestItem>>())
        verify(refreshPolicy).updateLastRefreshedTime()
    }

    @Test
    fun testRefreshError() {
        val error = RuntimeException("error")
        whenever(repo.getData()).thenReturn(Single.error<List<TestItem>>(error))
        logic.loadFirstPage()
        testUi.assertLastRendered(ListBlockState.Loading<List<TestItem>>())
        testScheduler.triggerActions()
        testUi.assertLastRendered(ListBlockState.Error<List<TestItem>>(error))
        verify(refreshPolicy, never()).updateLastRefreshedTime()
    }

    @Test
    fun testPagination() {
        logic.loadFirstPage()
        testUi.assertLastRendered(ListBlockState.Loading<List<TestItem>>())
        testScheduler.triggerActions()
        testUi.assertLastRendered(ListBlockState.DataReady<List<TestItem>>(testData))
        verify(refreshPolicy).updateLastRefreshedTime()

        // paginate once
        val page1 = listOf(TestItem())
        whenever(repo.paginate()).thenReturn(Maybe.just(page1))
        logic.paginate()
        testScheduler.triggerActions()
        testUi.assertLastRendered(ListBlockState.DataReady<List<TestItem>>(testData + page1))

        // paginate twice
        val page2 = listOf(TestItem())
        whenever(repo.paginate()).thenReturn(Maybe.just(page2))
        logic.paginate()
        testScheduler.triggerActions()
        testUi.assertLastRendered(ListBlockState.DataReady<List<TestItem>>(testData + page1 + page2))

        // refresh initial
        logic.loadFirstPage()
        testScheduler.triggerActions()
        testUi.assertLastRendered(ListBlockState.DataReady<List<TestItem>>(testData))
    }
}
