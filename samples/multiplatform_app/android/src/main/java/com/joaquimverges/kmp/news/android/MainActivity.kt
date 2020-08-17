package com.joaquimverges.kmp.news.android

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.core.assemble
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.kmp.news.CommonListLogic

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ui = Ui(layoutInflater)
        assemble(CommonListLogic() + ui)
        setContentView(ui.view)
    }

    // TODO in compose instead
    class Ui(inflater: LayoutInflater) : UiBlock<DataLoadState<String>, BlockEvent>(
            R.layout.main_activity,
            inflater
    ) {
        val label = findView<TextView>(R.id.label)

        override fun render(state: DataLoadState<String>) {
            when (state) {
                is DataLoadState.Loading -> label.text = "Loading..."
                is DataLoadState.Ready -> label.text = state.data
            }
        }

    }
}