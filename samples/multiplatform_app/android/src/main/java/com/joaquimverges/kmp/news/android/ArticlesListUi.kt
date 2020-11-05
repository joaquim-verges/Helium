package com.joaquimverges.kmp.news.android

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
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
import androidx.compose.ui.zIndex
import androidx.ui.tooling.preview.Preview
import com.joaquimverges.helium.core.event.EventDispatcher
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.kmp.news.data.Article
import com.joaquimverges.kmp.news.data.ArticleResponse
import com.joaquimverges.kmp.news.data.ArticleSource
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        children()
    }
}

data class MainListPosition(var scrollPosition: Int = 0, var scrollOffset: Int = 0)
val scrollPosition = MainListPosition()

fun resetScrollPosition() {
    scrollPosition.scrollPosition = 0
    scrollPosition.scrollOffset = 0
}

@Composable
fun List(
    model: DataLoadState.Ready<ArticleResponse>,
    eventDispatcher: EventDispatcher<ArticleListLogic.Event>
) {
    val prevListSize = remember { mutableStateOf(0) }
    val map = remember(model.data.articles) { computeItemMap(model.data.articles) }
    val fetchMorePosition = remember(map) { (map.size * .75f).toInt() }
    val scrollState: LazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition.scrollPosition,
        initialFirstVisibleItemScrollOffset = scrollPosition.scrollOffset
    )
    LazyColumnForIndexed(
        items = map.entries.toList(),
        state = scrollState
    ) { index, item ->
        onActive {
            if (index >= fetchMorePosition && prevListSize.value != model.data.articles.size) {
                prevListSize.value = model.data.articles.size
                eventDispatcher.pushEvent(ArticleListLogic.Event.FetchMore)
            }
        }

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp)) {
            item.value.forEach {
                Item(
                    article = it,
                    imgAspectRatio = if (item.value.size == 1) 16 / 9f else 4 / 3f,
                    modifier = Modifier.weight(1f).padding(6.dp)
                ) {
                    scrollPosition.scrollPosition = scrollState.firstVisibleItemIndex
                    scrollPosition.scrollOffset = scrollState.firstVisibleItemScrollOffset
                    eventDispatcher.pushEvent(ArticleListLogic.Event.ArticleClicked(it))
                }
            }
        }
    }
}

fun computeItemMap(articles: List<Article>): Map<Int, MutableList<Article>> {
    val itemMap = mutableMapOf<Int, MutableList<Article>>()
    var idx = 0
    articles.forEachIndexed { index, article ->
        if (index % 5 == 0) {
            itemMap[idx] = mutableListOf(article)
            idx += 1
        } else {
            val current = itemMap[idx]
            if (current == null) {
                itemMap[idx] = mutableListOf(article)
            } else {
                itemMap[idx]?.add(article)
                idx += 1
            }
        }
    }
    return itemMap
}

@Composable
fun Item(
    article: Article,
    imgAspectRatio: Float,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (image, source, title) = createRefs()
        NetworkImage(
            article.urlToImage,
            modifier = Modifier
                .aspectRatio(imgAspectRatio)
                .clip(RoundedCornerShape(5.dp))
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.clickable(onClick = onClick)
        )
        Text(
            article.source?.name
                ?: "",
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier.constrainAs(source) {
                top.linkTo(image.bottom)
                bottom.linkTo(image.bottom)
                start.linkTo(image.start, margin = 8.dp)
            }.background(
                color = Color.White,
                shape = RoundedCornerShape(4.dp)
            ).padding(horizontal = 6.dp, vertical = 4.dp)
                .zIndex(1f)
        )
        Text(
            article.title
                ?: "",
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier.constrainAs(title) {
                top.linkTo(source.bottom, margin = 4.dp)
                start.linkTo(image.start)
                bottom.linkTo(parent.bottom, margin = 8.dp)
            }
        )
    }
}

@Composable
fun NetworkImage(urlToImage: String?, modifier: Modifier) {
    urlToImage?.takeIf { it.isNotBlank() }?.let {
        CoilImage(
            modifier = modifier,
            data = urlToImage,
            contentScale = ContentScale.Crop,
            loading = {
                Box(modifier.background(Color.LightGray))
            }
        )
    } ?: run {
        Box(modifier.background(Color.LightGray))
    }
}

@Preview
@Composable
fun ItemPreview() {
    Surface {
        Item(
            article = Article(
                ArticleSource("", "Engadget", ""),
                "",
                "Article title with striking headline",
                "Article description",
                "Article content",
                "http://google.com",
                "",
                ""
            ),
            imgAspectRatio = 1f,
            onClick = {}
        )
    }
}
