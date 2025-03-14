package com.dherediat97.rickandmorty.presentation.characterlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.model.CharacterStatus
import com.dherediat97.rickandmorty.ui.theme.BlackColor
import com.dherediat97.rickandmorty.ui.theme.GreenColor
import com.dherediat97.rickandmorty.ui.theme.GreyColor
import com.dherediat97.rickandmorty.ui.theme.RedColor
import com.dherediat97.rickandmorty.ui.theme.WhiteColor

@Composable
fun CharacterCard(modifier: Modifier, character: Character) {

    val shape =  RoundedCornerShape(48.dp)
    val height = 100.dp

    Card(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth(),
    ) {
        Box(
            modifier = modifier
                .height(height)
                .fillMaxWidth()
                .background(Color.Transparent, shape = shape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(20.dp)
                    .align(Alignment.TopEnd)
                    .zIndex(1000f)
                    .clip(CircleShape)
                    .background(
                        color = when (character.status) {
                            CharacterStatus.Alive.name -> GreenColor
                            CharacterStatus.Dead.name -> RedColor
                            else -> GreyColor
                        }
                    )
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.image)
                    .size(Size.ORIGINAL)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = "Character Image",
            )

            Text(
                character.name,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(BlackColor.copy(alpha = 0.4f)),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
        }
    }

}