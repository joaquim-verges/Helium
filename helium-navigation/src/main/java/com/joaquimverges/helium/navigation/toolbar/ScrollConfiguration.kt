package com.joaquimverges.helium.navigation.toolbar

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

/**
 * Configures the scrolling behavior for a [CollapsingToolbarScreenViewDelegate]
 *
 * @param scrollMode: The scrolling more (see [ScrollMode])
 * @param toolbarCollapseMode: How the toolbar should behave on scroll (see [CollapseMode])
 * @param backdropCollapseMode: How the backdrop header should behave on scroll (see [CollapseMode])
 */
data class ScrollConfiguration(
    val scrollMode: ScrollMode,
    val toolbarCollapseMode: CollapseMode,
    val backdropCollapseMode: CollapseMode
) {

    companion object {
        fun noScroll() = ScrollConfiguration(
            ScrollMode.NO_SCROLL,
            CollapseMode.NONE,
            CollapseMode.NONE
        )

        fun default() = ScrollConfiguration(
            ScrollMode.SCROLL_OFF_AND_SCROLL_BACK_IN,
            CollapseMode.NONE,
            CollapseMode.NONE
        )

        fun defaultWithBackdrop() = ScrollConfiguration(
            ScrollMode.SCROLL_OFF_UNTIL_COLLAPSED,
            CollapseMode.PIN,
            CollapseMode.PARALLAX
        )
    }

    enum class ScrollMode(@AppBarLayout.LayoutParams.ScrollFlags val scrollFlags: Int) {
        NO_SCROLL(0),
        SCROLL_OFF(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL),
        SCROLL_OFF_UNTIL_COLLAPSED(
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
        ),
        SCROLL_OFF_AND_SCROLL_BACK_IN(
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        ),
        SCROLL_OFF_UNTIL_COLLAPSED_AND_SCROLL_BACK_IN(
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        ),
        SCROLL_OFF_AND_SCROLL_BACK_IN_COLLAPSED(
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED
        )
    }

    enum class CollapseMode(val mode: Int) {
        NONE(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF),
        PARALLAX(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX),
        PIN(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN)
    }
}