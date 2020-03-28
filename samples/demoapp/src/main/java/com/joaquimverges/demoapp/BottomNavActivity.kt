package com.joaquimverges.demoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joaquimverges.helium.navigation.bottomnav.BottomNavUi

class BottomNavActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ui = BottomNavUi(layoutInflater, R.menu.my_menu, R.navigation.my_graph)
        setContentView(ui.view)
    }

}
