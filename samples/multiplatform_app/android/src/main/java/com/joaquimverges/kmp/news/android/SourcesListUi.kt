package com.joaquimverges.kmp.news.android

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joaquimverges.helium.core.event.EventDispatcher
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.kmp.news.data.SourceWithSelection
import com.joaquimverges.kmp.news.data.models.ArticleSource
import com.joaquimverges.kmp.news.logic.SourcesListLogic

@Composable
fun SourcesListUi(
    state: DataLoadState<SourceWithSelection>?,
    eventDispatcher: EventDispatcher<SourcesListLogic.Event>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Select News Sources",
                        style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Black)
                    )
                },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp,
                modifier = Modifier.height(72.dp),
                navigationIcon = {
                    IconButton(
                        onClick = { eventDispatcher.pushEvent(SourcesListLogic.Event.CloseClicked) }
                    ) {
                        Image(
                            asset = Icons.Filled.ArrowBack,
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                        )
                    }
                }
            )
        }
    ) {
        when (state) {
            is DataLoadState.Init, is DataLoadState.Loading, null -> {
                Centered {
                    CircularProgressIndicator(Modifier.size(48.dp))
                }
            }
            is DataLoadState.Empty -> {
                Centered {
                    Text("No Sources Found")
                }
            }
            is DataLoadState.Error -> {
                Centered {
                    Text(
                        "Network Error: ${state.error.message}",
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
            is DataLoadState.Ready -> {
                LazyColumnFor(state.data.sources, modifier = Modifier.fillMaxWidth()) { item ->
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val selected = mutableStateOf(state.data.selectedMap[item.id] ?: false)
                        Text(text = item.name ?: "", modifier = Modifier.weight(1f))
                        Checkbox(checked = selected.value, onCheckedChange = { checked ->
                            selected.value = checked
                            if (checked) {
                                eventDispatcher.pushEvent(
                                    SourcesListLogic.Event.SourceSelected(item)
                                )
                            } else {
                                eventDispatcher.pushEvent(
                                    SourcesListLogic.Event.SourceUnselected(
                                        item
                                    )
                                )
                            }
                        })
                    }
                }
            }
        }
    }
}