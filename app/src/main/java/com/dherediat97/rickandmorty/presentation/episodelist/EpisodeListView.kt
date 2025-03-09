package com.dherediat97.rickandmorty.presentation.episodelist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dherediat97.rickandmorty.R
import com.dherediat97.rickandmorty.domain.model.Episode
import org.koin.androidx.compose.koinViewModel


@Composable
fun Episodes(episodeViewModel: EpisodeListViewModel = koinViewModel()) {
    val episodes: List<Episode> by episodeViewModel.episodeList.collectAsStateWithLifecycle(
        initialValue = emptyList(),
    )

    Text(
        "Episodes", modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
        textAlign = TextAlign.Center
    )

    LazyVerticalGrid(columns = GridCells.Adaptive(120.dp), content = {
        items(episodes) { item ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.background_episode),
                    contentDescription = item.episode,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(Modifier.fillMaxWidth()) {
                    BasicText(
                        "${item.episode} - ${item.name}",
                        color = { Color.White },
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 8.sp,
                            maxFontSize = 14.sp,
                            stepSize = 2.sp
                        ),
                    )
                }
            }
        }
    })
}
