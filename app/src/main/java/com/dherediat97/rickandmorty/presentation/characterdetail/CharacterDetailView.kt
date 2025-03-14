package com.dherediat97.rickandmorty.presentation.characterdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.dherediat97.rickandmorty.domain.model.CharacterInfo
import com.dherediat97.rickandmorty.domain.model.CharacterStatus
import com.dherediat97.rickandmorty.ui.theme.GreenColor
import com.dherediat97.rickandmorty.ui.theme.GreyColor
import com.dherediat97.rickandmorty.ui.theme.RedColor


@Composable
fun CharacterDetailView(characterDetailViewModel: CharacterDetailViewModel) {
    val characterInfo: CharacterInfo by characterDetailViewModel.characterInfo.collectAsStateWithLifecycle(
        initialValue = CharacterInfo(),
    )

    Column {
        AsyncImage(
            model = characterInfo.image, modifier = Modifier
                .fillMaxWidth()
                .height(300.dp), contentDescription = characterInfo.name,
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = characterInfo.name, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                style = TextStyle(
                    fontSize = 35.sp, fontWeight = FontWeight.Bold,
                    color = when (characterInfo.status) {
                        CharacterStatus.Alive.name -> GreenColor
                        CharacterStatus.Dead.name -> RedColor
                        else -> GreyColor
                    }
                ),
                textAlign = TextAlign.Center
            )
            Text(text = "Gender: " + characterInfo.gender)
            Text(text = "Species: " + characterInfo.species)
            Text(text = "Location: " + characterInfo.location.name)


//            Episodes(episodeViewModel = koinViewModel())
        }
    }
}