package com.joaquimverges.helium.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.helium.test.HeliumUiTestCase
import com.joaquimverges.helium.test.TestLogicBLock
import com.joaquimverges.helium.ui.R
import com.joaquimverges.helium.ui.list.adapter.ListItem
import com.joaquimverges.helium.ui.list.event.ListBlockEvent
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [ListUi]
 */
class ListUiTest : HeliumUiTestCase() {

    data class TestData(val value: Int)

    private val logic = TestLogicBLock<DataLoadState<List<TestData>>, ListBlockEvent<BlockEvent>>()
    private lateinit var ui: ListUi<TestData, BlockEvent, TestListItem>

    @Before
    fun setup() {
        onActivity {
            ui = ListUi(LayoutInflater.from(it), { _, _ ->
                TestListItem(it)
            })
            it.setContentView(ui.view)
            (logic + ui).assemble()
        }
    }

    @Test
    fun `test successful load`() {
        logic.pushState(DataLoadState.Init())
        verifyViewHidden(R.id.loader)
        verifyViewHidden(R.id.empty_view_container)
        verifyViewHidden(R.id.error_view_container)
        logic.pushState(DataLoadState.Loading())
        verifyViewVisible(R.id.loader)
        logic.pushState(DataLoadState.Ready(listOf(TestData(1))))
        verifyViewHidden(R.id.loader)
        onView(withId(R.id.recycler_view)).check(
            matches(
                withChild(withText("item 1"))
            )
        )
    }

    @Test
    fun `test successful load then pagination`() {
        val firstPage = listOf(TestData(1), TestData(2))
        val secondPage = firstPage + listOf(TestData(3), TestData(4))
        logic.pushState(DataLoadState.Init())
        verifyViewHidden(R.id.loader)
        verifyViewHidden(R.id.empty_view_container)
        verifyViewHidden(R.id.error_view_container)
        logic.pushState(DataLoadState.Loading())
        verifyViewVisible(R.id.loader)
        logic.pushState(DataLoadState.Ready(firstPage))
        verifyViewHidden(R.id.loader)
        onView(withId(R.id.recycler_view)).check(
            matches(
                allOf(
                    withChild(withText("item 1")),
                    withChild(withText("item 2"))
                )
            )
        )
        logic.pushState(DataLoadState.Loading())
        verifyViewHidden(R.id.loader)
        logic.pushState(DataLoadState.Ready(secondPage))
        Thread.sleep(1000) // wait for async diff util
        onView(withId(R.id.recycler_view)).check(
            matches(
                allOf(
                    withChild(withText("item 1")),
                    withChild(withText("item 2")),
                    withChild(withText("item 3")),
                    withChild(withText("item 4"))
                )
            )
        )
    }

    @Test
    fun `test load empty`() {
        logic.pushState(DataLoadState.Init())
        verifyViewHidden(R.id.loader)
        verifyViewHidden(R.id.empty_view_container)
        verifyViewHidden(R.id.error_view_container)
        logic.pushState(DataLoadState.Loading())
        verifyViewVisible(R.id.loader)
        logic.pushState(DataLoadState.Empty())
        verifyViewHidden(R.id.loader)
        verifyViewVisible(R.id.empty_view_container)
    }

    @Test
    fun `test load error`() {
        logic.pushState(DataLoadState.Init())
        verifyViewHidden(R.id.loader)
        verifyViewHidden(R.id.empty_view_container)
        verifyViewHidden(R.id.error_view_container)
        logic.pushState(DataLoadState.Loading())
        verifyViewVisible(R.id.loader)
        logic.pushState(DataLoadState.Error(Throwable()))
        verifyViewHidden(R.id.loader)
        verifyViewVisible(R.id.error_view_container)
    }

    class TestListItem(context: Context) : ListItem<TestData, BlockEvent>(TextView(context)) {
        override fun bind(data: TestData) {
            (view as TextView).text = "item ${data.value}"
        }
    }
}
