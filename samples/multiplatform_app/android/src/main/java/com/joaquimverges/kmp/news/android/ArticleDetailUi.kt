package com.joaquimverges.kmp.news.android

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joaquimverges.helium.core.event.EventDispatcher
import com.joaquimverges.kmp.news.android.utils.Draggable
import com.joaquimverges.kmp.news.data.models.Article
import com.joaquimverges.kmp.news.data.models.ArticleSource
import com.joaquimverges.kmp.news.logic.ArticleDetailLogic
import com.google.accompanist.coil.CoilImage

@Composable
fun ArticleDetailUi(
    state: ArticleDetailLogic.DetailState?,
    dispatcher: EventDispatcher<ArticleDetailLogic.DetailEvent>
) {
    Draggable(
        onSwiped = {
            dispatcher.pushEvent(ArticleDetailLogic.DetailEvent.ArticleClosed)
        }
    ) {
        ArticleDetailContent(
            state,
            dispatcher
        )
    }
}

@Composable
fun ArticleDetailContent(
    state: ArticleDetailLogic.DetailState?,
    dispatcher: EventDispatcher<ArticleDetailLogic.DetailEvent>
) {
    state?.article?.let { article ->
        Column(
            Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.background)
        ) {
            HeroImage(
                article.urlToImage,
                dispatcher
            )
            Column(
                Modifier.fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = article.source?.name ?: "",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = article.title ?: "",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    article.description
                        ?: "",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                article.url?.let { url ->
                    Text(
                        "Read More",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.clickable(
                            onClick = {
                                dispatcher.pushEvent(ArticleDetailLogic.DetailEvent.ReadMoreClicked(url))
                            }
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun HeroImage(
    imageUrl: String?,
    dispatcher: EventDispatcher<ArticleDetailLogic.DetailEvent>
) {
    imageUrl?.let {
        CoilImage(
            data = it,
            contentDescription = "Article Image",
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(4 / 3f)
                .clickable(
                    onClick = {
                        dispatcher.pushEvent(ArticleDetailLogic.DetailEvent.ArticleClosed)
                    }
                ),
            loading = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .aspectRatio(4 / 3f)
                        .background(MaterialTheme.colors.secondary)
                )
            },
            contentScale = ContentScale.Crop
        )
    } ?: run {
        Box(
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(4 / 3f)
                .background(MaterialTheme.colors.secondary)
        )
    }
}

@Preview
@Composable
fun preview() {
    MaterialTheme(colors = Themes.light) {
        Surface {
            ArticleDetailContent(
                state = ArticleDetailLogic.DetailState(
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
                        null,
                        ""
                    )
                ),
                dispatcher = EventDispatcher()
            )
        }
    }
}
