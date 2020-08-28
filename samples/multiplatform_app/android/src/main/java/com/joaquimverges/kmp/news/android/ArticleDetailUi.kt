package com.joaquimverges.kmp.news.android

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joaquimverges.kmp.news.logic.ArticleDetailLogic
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun ArticleDetailUi(state: ArticleDetailLogic.DetailState?) {
    state?.article?.let { article ->
        ScrollableColumn(Modifier.fillMaxSize()) {
            CoilImage(
                    modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9f),
                    data = article.urlToImage ?: "",
                    contentScale = ContentScale.Crop
            )
            Column(Modifier.fillMaxWidth().padding(24.dp)) {
                Text(article.title
                        ?: "", style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium))
                Spacer(modifier = Modifier.height(24.dp))
                Text(article.description
                        ?: "", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal))
            }
        }
    }
}