package com.joaquimverges.demoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SimpleListFragmentActivityRetained : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_container)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, ListFragment()).commit()
        }
    }
}

