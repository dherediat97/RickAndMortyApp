package com.dherediat97.rickandmorty.presentation.characterdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dherediat97.rickandmorty.presentation.episode.Episodes
import com.dherediat97.rickandmorty.presentation.error.ErrorComposableView
import com.dherediat97.rickandmorty.presentation.loading.LoadingComposableView
import org.koin.androidx.compose.koinViewModel


@Composable
fun CharacterDetailScreen(
    characterId: Int,
    viewModel: CharacterDetailViewModel = koinViewModel()) {


    LaunchedEffect(Unit) {
        viewModel.fetchCharacter(characterId)
    }

    val data by viewModel.uiState.collectAsState()

    if (data.isLoading) {
        LoadingComposableView()
    } else {
        if (data.error)
            ErrorComposableView()
        else {
            val character = data.character
            if (character != null) {
                Column {
                    AsyncImage(model = character.image, modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp), contentDescription = character.name,
                        contentScale = ContentScale.FillWidth)
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)) {
                        Text(text = character.name, modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp),
                            style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center)

                        Text(text = "Details", modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 4.dp),
                            style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center)
                        Text(text = "Status: " + character.status)
                        Text(text = "Gender: " + character.gender)
                        Text(text = "Species: " + character.species)
                        Text(text = "Last Location: " + character.location.name)

                        Text("Episodes", modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp),
                            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center)
                        Episodes(character.episode)
                    }

                }
            }
        }
    }
}