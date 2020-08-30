package com.joaquimverges.demoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joaquimverges.helium.ui.viewpager.PagerUi

/**
 * @author joaqu
 */
class ViewPagerFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PagerUi(
            requireActivity(), ViewPagerActivity.MyPageProvider(),
            viewPagerConfig = {
                it.pageMargin = resources.getDimensionPixelSize(R.dimen.menu_padding)
            }
        ).view
    }
}
