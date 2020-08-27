package com.joaquimverges.kmp.news.android

import android.content.Context
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.kmp.news.Article
import com.joaquimverges.kmp.news.ArticleResponse
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade

class ArticlesListUi(context: Context) : ComposeUiBlock<DataLoadState<ArticleResponse>, BlockEvent>(context) {

    @Composable
    override fun Content(model: DataLoadState<ArticleResponse>?) {
        when (model) {
            is DataLoadState.Init -> {

            }
            is DataLoadState.Loading -> {

            }
            is DataLoadState.Empty -> {

            }
            is DataLoadState.Error -> {

            }
            is DataLoadState.Ready -> {
                list(model)
            }
            null -> {

            }
        }
    }

    @Composable
    fun list(model: DataLoadState.Ready<ArticleResponse>) {
        Scaffold(topBar = {
            TopAppBar(title = {
                Text("Helium News", style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Black))
            }, backgroundColor = Color.White, modifier = Modifier.height(72.dp))
        }) {
            LazyColumnFor(items = model.data.articles) {
                item(article = it)
            }
        }
    }

    @Composable
    fun item(article: Article) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp), verticalGravity = Alignment.CenterVertically) {
            CoilImageWithCrossfade(
                    modifier = Modifier.size(100.dp).clip(RoundedCornerShape(5.dp)),
                    data = article.urlToImage ?: "",
                    contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(article.source?.name
                        ?: "", style = TextStyle(fontSize = 16.sp, color = Color.DarkGray))
                Spacer(modifier = Modifier.height(5.dp))
                Text(article.title
                        ?: "", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium))
            }
        }
    }
}