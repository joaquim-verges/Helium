package com.joaquimverges.kmp.news.android

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.joaquimverges.helium.core.event.EventDispatcher
import com.joaquimverges.kmp.news.android.utils.Draggable
import com.joaquimverges.kmp.news.data.Article
import com.joaquimverges.kmp.news.data.ArticleSource
import com.joaquimverges.kmp.news.logic.ArticleDetailLogic
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun ArticleDetailUi(
    state: ArticleDetailLogic.DetailState?,
    dispatcher: EventDispatcher<ArticleDetailLogic.DetailEvent>
) {
    Draggable(onSwiped = {
        dispatcher.pushEvent(ArticleDetailLogic.DetailEvent.ArticleClosed)
    }) {
        ArticleDetailContent(state, dispatcher)
    }
}

@Composable
fun ArticleDetailContent(
    state: ArticleDetailLogic.DetailState?,
    dispatcher: EventDispatcher<ArticleDetailLogic.DetailEvent>
) {
    state?.article?.let { article ->
        ScrollableColumn(Modifier.fillMaxSize()) {
            CoilImage(
                modifier = Modifier.fillMaxWidth()
                    .aspectRatio(4 / 3f)
                    .clickable(
                        onClick = {
                            dispatcher.pushEvent(ArticleDetailLogic.DetailEvent.ArticleClosed)
                        },
                        indication = RippleIndication()
                    ),
                data = article.urlToImage ?: "",
                loading = {
                    Box(modifier = Modifier.fillMaxWidth()
                        .aspectRatio(4 / 3f)
                        .background(Color.LightGray))
                },
                contentScale = ContentScale.Crop
            )
            Column(Modifier.fillMaxWidth().padding(24.dp)) {
                Text(
                    text = article.title ?: "",
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    article.description
                        ?: "",
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal)
                )
                Spacer(modifier = Modifier.height(24.dp))
                article.url?.let { url ->
                    Text(
                        "Read More",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.clickable(onClick = {
                            dispatcher.pushEvent(ArticleDetailLogic.DetailEvent.ReadMoreClicked(url))
                        }, indication = RippleIndication(bounded = false))
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun preview() {
    Surface {
        ArticleDetailContent(
            state = ArticleDetailLogic.DetailState(
                article = Article(
                    ArticleSource("", "Engadget", ""),
                    "",
                    "Article title with striking headline",
                    "Article description",
                    "Article content",
                    "http://google.com",
                    "https://www.tesla.com/sites/default/files/modelsx-new/social/model-x-social.jpg",
                    ""
                )
            ), dispatcher = EventDispatcher())
    }
}