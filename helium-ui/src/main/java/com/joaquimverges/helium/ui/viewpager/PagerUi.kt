package com.joaquimverges.helium.ui.viewpager

import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.viewpager.widget.ViewPager
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.ui.R

/**
 * A simple ViewPager UiBlock
 */
open class PagerUi(
    activity: FragmentActivity,
    fragmentPageProvider: FragmentPageProvider,
    // optional layout properties
    layoutResId: Int = R.layout.view_pager,
    container: ViewGroup? = null,
    addToContainer: Boolean = false,
    // optional view pager config
    viewPagerConfig: ((ViewPager) -> Unit)? = null
) : UiBlock<BlockState, BlockEvent>(layoutResId, activity.layoutInflater, container, addToContainer) {

    private val maxRetainedPages = 5
    private val pager: ViewPager = view.findViewById(R.id.view_pager)

    init {
        viewPagerConfig?.invoke(pager)
        pager.adapter = if (fragmentPageProvider.getCount() > maxRetainedPages) {
            // if more than 5 pages, switch to a StatePagerAdapter which is much more efficient
            BaseStatePagerAdapter(activity.supportFragmentManager, fragmentPageProvider)
        } else {
            // if 5 pages or less (typically tabbed navigation) use a regular PagerAdapter
            BasePagerAdapter(activity.supportFragmentManager, fragmentPageProvider)
        }
    }

    interface FragmentPageProvider {
        fun getFragmentAt(position: Int): Fragment
        fun getCount(): Int
    }

    class BasePagerAdapter(fm: FragmentManager, private val fragmentPageProvider: FragmentPageProvider) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int) = fragmentPageProvider.getFragmentAt(position)
        override fun getCount() = fragmentPageProvider.getCount()
    }

    class BaseStatePagerAdapter(fm: FragmentManager, private val fragmentPageProvider: FragmentPageProvider) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int) = fragmentPageProvider.getFragmentAt(position)
        override fun getCount() = fragmentPageProvider.getCount()
    }

    override fun render(blockState: BlockState) {
        // for subclasses to render more things if needed
    }
}