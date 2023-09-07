package com.dherediat97.rickandmorty.presentation.episode


import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dherediat97.rickandmorty.R
import org.koin.androidx.compose.koinViewModel


@Composable
fun Episodes(episodes: List<String>, viewModel: EpisodeListViewModel = koinViewModel()) {
    val data by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchEpisodes(episodes)
    }


    if (!data.isLoading && !data.error) {
        val items by remember { mutableStateOf(data.episodes) }

        LazyVerticalGrid(columns = GridCells.Adaptive(100.dp), contentPadding = PaddingValues(
            start = 2.dp,
            end = 2.dp,
            bottom = 8.dp
        ), content = {
            items(items) { episode ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)) {
                    Image(
                        painterResource(id = R.drawable.background_episode),
                        contentDescription = episode.episode, modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.60f))
                    Text(episode.episode, textAlign = TextAlign.End, modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp, bottom = 8.dp))
                }
//                Card(modifier = Modifier
//                    .fillMaxWidth()
//                    .height(20.dp)
//                    .padding(8.dp)){
//                    Text(episode.name, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
//                }
            }
        })

    }
}