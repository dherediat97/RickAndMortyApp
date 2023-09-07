package com.dherediat97.rickandmorty.presentation.characterlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.model.CharacterStatus
import com.dherediat97.rickandmorty.ui.theme.Red
import java.util.Locale

@Composable
fun CharacterCard(modifier: Modifier, character: Character, onNavigateCharacter: (Int) -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onNavigateCharacter(character.id) }
    ) {
        SubcomposeAsyncImage(model = ImageRequest.Builder(LocalContext.current)
            .data(character.image)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .build(), modifier = Modifier.fillMaxWidth(),
            contentDescription = character.name,
            loading = {
                CircularProgressIndicator(modifier = Modifier.padding(12.dp).fillMaxWidth().size(50.dp))
            },
            contentScale = ContentScale.FillWidth)
        Column(modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 12.dp)) {
            Text(character.name, modifier = Modifier.fillMaxWidth(),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White),
                maxLines = 1, softWrap = true, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center
            )
            Text(character.status.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(fontSize = 16.sp, color =
                when (character.status) {
                    CharacterStatus.Alive.name -> Color.Green
                    CharacterStatus.Dead.name -> Red
                    else -> Color.White
                }),
                textAlign = TextAlign.Center
            )
        }
    }
}