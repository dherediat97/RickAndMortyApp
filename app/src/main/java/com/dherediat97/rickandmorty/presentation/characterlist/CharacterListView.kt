package com.dherediat97.rickandmorty.presentation.characterlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.model.ResponsePaginate

@Composable
fun CharacterListView(
    characterListViewModel: CharacterListViewModel,
    onNavigateCharacter: (Character) -> Unit,
) {
    var page: Int by remember { mutableIntStateOf(1) }
    val responsePaginateCharacters: ResponsePaginate<Character> by characterListViewModel.characterList.collectAsStateWithLifecycle(
        initialValue = ResponsePaginate()
    )
    val characterList = responsePaginateCharacters.results
    val listState = rememberLazyGridState()

    LazyVerticalGrid(state = listState, columns = GridCells.Adaptive(120.dp), content = {
        items(
            items = characterList
        ) { character ->

            CharacterCard(
                modifier = Modifier.clickable(onClick = {
                    onNavigateCharacter(character)
                }),
                character = character,
            )
        }
    })
}