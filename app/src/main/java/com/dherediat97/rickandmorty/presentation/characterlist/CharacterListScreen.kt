package com.dherediat97.rickandmorty.presentation.characterlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.dherediat97.rickandmorty.presentation.characterdetail.CharacterCard
import com.dherediat97.rickandmorty.presentation.error.ErrorComposableView
import com.dherediat97.rickandmorty.presentation.loading.LoadingComposableView
import org.koin.androidx.compose.koinViewModel


@Composable
fun CharacterListScreen(viewModel: CharacterListViewModel = koinViewModel(), onNavigateCharacter: (Int) -> Unit) {
    val data by viewModel.uiState.collectAsState()
    val page by remember { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        viewModel.fetchCharacters(page)
    }

    if (data.isLoading)
        LoadingComposableView()
    else {
        if (data.error) ErrorComposableView()

        val items by remember { mutableStateOf(data.characters) }
        LazyVerticalGrid(columns = GridCells.Adaptive(128.dp), contentPadding = PaddingValues(
            start = 8.dp,
            top = 16.dp,
            end = 8.dp,
            bottom = 16.dp
        ), content = {
            items(items) { character ->
                CharacterCard(character) {
                    onNavigateCharacter(character.id)
                }
            }
        })
    }
}