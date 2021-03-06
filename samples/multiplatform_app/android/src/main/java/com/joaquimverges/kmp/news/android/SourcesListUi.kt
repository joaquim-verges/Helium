package com.joaquimverges.kmp.news.android

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.joaquimverges.kmp.news.Sources
import com.joaquimverges.kmp.news.logic.SourcesListLogic

@Composable
fun SourcesListUi(
    state: DataLoadState<List<Sources>>?,
    eventDispatcher: EventDispatcher<SourcesListLogic.Event>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Select News Sources",
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black
                        )
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
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Go back",
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
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(state.data) { item ->
                        Row(
                            modifier = Modifier.padding(
                                horizontal = 24.dp,
                                vertical = 12.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val selected = mutableStateOf(item.selected)
                            Text(
                                text = item.name ?: "",
                                modifier = Modifier.weight(1f)
                            )
                            Checkbox(
                                checked = selected.value,
                                onCheckedChange = { checked ->
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
}