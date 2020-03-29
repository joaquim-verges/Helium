package com.joaquimverges.demoapp

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.joaquimverges.helium.navigation.bottomnav.BottomNavUi

class BottomNavActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ui = BottomNavUi(layoutInflater, R.menu.my_menu, R.navigation.my_graph, bottomBarCustomization = {
            val colors = ContextCompat.getColorStateList(this, R.color.bottom_tab_colors)
            it.itemIconTintList = colors
            it.itemTextColor = colors
        })
        setContentView(ui.view)
    }

}
