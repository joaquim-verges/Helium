package com.joaquimverges.helium.ui.list

import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.helium.test.HeliumTestCase
import com.joaquimverges.helium.test.TestUiBlock
import com.joaquimverges.helium.ui.list.event.ListBlockEvent
import com.joaquimverges.helium.ui.list.repository.ListRepository
import com.joaquimverges.helium.ui.util.RefreshPolicy
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

/**
 * Unit tests for [ListLogic]
 */
class ListLogicTest : HeliumTestCase() {

    class TestItem

    @Mock lateinit var refreshPolicy: RefreshPolicy
    @Mock lateinit var repo: ListRepository<List<TestItem>>

    private lateinit var logic: ListLogic<TestItem, BlockEvent>
    private val testUi = TestUiBlock<DataLoadState<List<TestItem>>, ListBlockEvent<BlockEvent>>()
    private val testData = (0..20).map { TestItem() }.toList()

    @Before
    fun setup() {
        logic = ListLogic(repo, refreshPolicy, coroutinesTestRule.testDispatcher)
        assemble(logic + testUi)
    }

    @Test
    fun testRefreshPolicy() = runBlockingTest {
        repo.stub { onBlocking { getFirstPage() }.doReturn(testData) }
        whenever(refreshPolicy.shouldRefresh()).thenReturn(false)
        logic.refreshIfNeeded()
        testUi.assertLastRendered(DataLoadState.Init<TestItem>())
        whenever(refreshPolicy.shouldRefresh()).thenReturn(true)
        logic.refreshIfNeeded()
        testUi.assertLastRendered(DataLoadState.Ready(testData))
    }

    @Test
    fun testRefreshWithData() = runBlockingTest {
        repo.stub { onBlocking { getFirstPage() }.doReturn(testData) }
        logic.loadFirstPage()
        testUi.assertLastRendered(DataLoadState.Ready(testData))
        verify(refreshPolicy).updateLastRefreshedTime()
    }

    @Test
    fun testRefreshWithNoData() = runBlockingTest {
        repo.stub { onBlocking { getFirstPage() }.doReturn(emptyList()) }
        logic.loadFirstPage()
        testUi.assertLastRendered(DataLoadState.Empty<List<TestItem>>())
        verify(refreshPolicy).updateLastRefreshedTime()
    }

    @Test
    fun testRefreshError() = runBlockingTest {
        val error = RuntimeException("error")
        repo.stub { onBlocking { getFirstPage() }.doThrow(error) }
        logic.loadFirstPage()
        testUi.assertLastRendered(DataLoadState.Error<List<TestItem>>(error))
        verify(refreshPolicy, never()).updateLastRefreshedTime()
    }

    @Test
    fun testPagination() = runBlockingTest {
        repo.stub { onBlocking { getFirstPage() }.doReturn(testData) }
        logic.loadFirstPage()
        testUi.assertLastRendered(DataLoadState.Ready(testData))
        verify(refreshPolicy).updateLastRefreshedTime()

        // paginate once
        val page1 = listOf(TestItem())
        repo.stub { onBlocking { paginate() }.doReturn(page1) }
        logic.paginate()
        testUi.assertLastRendered(DataLoadState.Ready(testData + page1))

        // paginate twice
        val page2 = listOf(TestItem())
        repo.stub { onBlocking { paginate() }.doReturn(page2) }
        logic.paginate()
        testUi.assertLastRendered(DataLoadState.Ready(testData + page1 + page2))

        // refresh initial
        logic.loadFirstPage()
        testUi.assertLastRendered(DataLoadState.Ready(testData))
    }
}
