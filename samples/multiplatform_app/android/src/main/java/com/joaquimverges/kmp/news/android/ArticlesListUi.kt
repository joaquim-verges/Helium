package com.joaquimverges.kmp.news.android

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.joaquimverges.helium.core.event.EventDispatcher
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.kmp.news.data.models.Article
import com.joaquimverges.kmp.news.data.models.ArticleResponse
import com.joaquimverges.kmp.news.data.models.ArticleSource
import com.joaquimverges.kmp.news.logic.ArticleListLogic
import com.google.accompanist.coil.CoilImage

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
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black
                        )
                    )
                },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp,
                modifier = Modifier.height(72.dp),
                actions = {
                    IconButton(
                        onClick = { eventDispatcher.pushEvent(ArticleListLogic.Event.AddSourcesClicked) },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Image(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Add Sources",
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
                    Text("No Articles Found")
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
                List(
                    state,
                    eventDispatcher
                )
            }
        }
    }
}

@Composable
fun Centered(children: @Composable () -> Unit) {
    Column(
        Modifier.fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        children()
    }
}

data class MainListPosition(
    var scrollPosition: Int = 0,
    var scrollOffset: Int = 0
)

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
    val context = LocalContext.current
    LazyColumn(
        state = scrollState
    ) {
        itemsIndexed(map.entries.toList()) { index, item ->
            SideEffect {
                if (index >= fetchMorePosition && prevListSize.value != model.data.articles.size) {
                    prevListSize.value = model.data.articles.size
                    eventDispatcher.pushEvent(ArticleListLogic.Event.FetchMore)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 6.dp)
            ) {
                item.value.forEach {
                    Item(
                        article = it,
                        imgAspectRatio = if (item.value.size == 1) 16 / 9f else 4 / 3f,
                        modifier = Modifier.weight(1f)
                            .padding(6.dp)
                    ) {
                        scrollPosition.scrollPosition = scrollState.firstVisibleItemIndex
                        scrollPosition.scrollOffset = scrollState.firstVisibleItemScrollOffset
                        eventDispatcher.pushEvent(ArticleListLogic.Event.ArticleClicked(it))
                    }
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
                }
                .clickable(onClick = onClick)
        )
        Text(
            article.source?.name
                ?: "",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.constrainAs(source) {
                top.linkTo(image.bottom)
                bottom.linkTo(image.bottom)
                start.linkTo(
                    image.start,
                    margin = 8.dp
                )
            }
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(
                    horizontal = 6.dp,
                    vertical = 4.dp
                )
                .zIndex(1f)
        )
        Text(
            article.title
                ?: "",
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.constrainAs(title) {
                top.linkTo(
                    source.bottom,
                    margin = 4.dp
                )
                start.linkTo(image.start)
                bottom.linkTo(
                    parent.bottom,
                    margin = 8.dp
                )
            }
        )
    }
}

@Composable
fun NetworkImage(
    urlToImage: String?,
    modifier: Modifier
) {
    urlToImage?.takeIf { it.isNotBlank() }
        ?.let {
            CoilImage(
                data = urlToImage,
                contentDescription = "Article thumbnail",
                modifier = modifier,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(modifier.background(MaterialTheme.colors.secondary))
                }
            )
        } ?: run {
        Box(modifier.background(MaterialTheme.colors.secondary))
    }
}

@Preview
@Composable
fun ItemPreview() {
    MaterialTheme(colors = darkColors()) {
        Surface {
            Item(
                article = Article(
                    ArticleSource(
                        "",
                        "Engadget",
                        ""
                    ),
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
}
