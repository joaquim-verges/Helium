package com.joaquimverges.kmp.news.android

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onActive
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joaquimverges.helium.core.event.EventDispatcher
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.kmp.news.data.Article
import com.joaquimverges.kmp.news.data.ArticleResponse
import com.joaquimverges.kmp.news.logic.ArticleListLogic
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun ArticleListUI(
    state: DataLoadState<ArticleResponse>?,
    eventDispatcher: EventDispatcher<ArticleListLogic.Event>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Helium News",
                        style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Black)
                    )
                },
                backgroundColor = Color.White, modifier = Modifier.height(72.dp)
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
                    Text("No Articles Found")
                }
            }
            is DataLoadState.Error -> {
                Centered {
                    Text("Network Error: ${state.error.message}")
                }
            }
            is DataLoadState.Ready -> {
                List(state, eventDispatcher)
            }
        }
    }
}

@Composable
fun Centered(children: @Composable () -> Unit) {
    Column(
        Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalGravity = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        children()
    }
}

@Composable
fun List(
    model: DataLoadState.Ready<ArticleResponse>,
    eventDispatcher: EventDispatcher<ArticleListLogic.Event>
) {
    val prevListSize = remember { mutableStateOf(0) }
    val fetchMorePosition = remember(model.data.articles) { (model.data.articles.size * .75f).toInt() }
    LazyColumnForIndexed(
        items = model.data.articles
    ) { index, item ->
        onActive {
            if (index >= fetchMorePosition && prevListSize.value != model.data.articles.size) {
                prevListSize.value = model.data.articles.size
                eventDispatcher.pushEvent(ArticleListLogic.Event.FetchMore)
            }
        }
        Item(article = item, eventDispatcher)
    }
}

@Composable
fun Item(article: Article, eventDispatcher: EventDispatcher<ArticleListLogic.Event>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    eventDispatcher.pushEvent(ArticleListLogic.Event.ArticleClicked(article))
                },
                indication = RippleIndication()
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalGravity = Alignment.CenterVertically
    ) {
        CoilImage(
            modifier = Modifier.size(100.dp).clip(RoundedCornerShape(5.dp)),
            data = article.urlToImage ?: "",
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(24.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                article.source?.name
                    ?: "",
                style = TextStyle(fontSize = 16.sp, color = Color.DarkGray)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                article.title
                    ?: "",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium)
            )
        }
    }
}
