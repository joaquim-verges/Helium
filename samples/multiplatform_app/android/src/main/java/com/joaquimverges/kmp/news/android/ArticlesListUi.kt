package com.joaquimverges.kmp.news.android

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
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
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                    "Helium News",
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Black)
            )
        }, backgroundColor = Color.White, modifier = Modifier.height(72.dp))
    }) {
        when (state) {
            is DataLoadState.Init, is DataLoadState.Loading, null -> {
                centered {
                    CircularProgressIndicator(Modifier.size(48.dp))
                }
            }
            is DataLoadState.Empty -> {
                centered {
                    Text("No Articles Found")
                }

            }
            is DataLoadState.Error -> {
                centered {
                    Text("Network Error")
                }
            }
            is DataLoadState.Ready -> {
                list(state, eventDispatcher)
            }
        }
    }
}

@Composable
fun centered(children: @Composable () -> Unit) {
    Column(
            Modifier.fillMaxWidth().fillMaxHeight(),
            horizontalGravity = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        children()
    }
}

@Composable
fun list(model: DataLoadState.Ready<ArticleResponse>, eventDispatcher: EventDispatcher<ArticleListLogic.Event>) {
    LazyColumnFor(items = model.data.articles) {
        item(article = it, eventDispatcher)
    }
}

@Composable
fun item(article: Article, eventDispatcher: EventDispatcher<ArticleListLogic.Event>) {
    Row(modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { eventDispatcher.pushEvent(ArticleListLogic.Event.ArticleClicked(article)) })
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
            Text(article.source?.name
                    ?: "", style = TextStyle(fontSize = 16.sp, color = Color.DarkGray))
            Spacer(modifier = Modifier.height(5.dp))
            Text(article.title
                    ?: "", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium))
        }
    }
}