package com.dherediat97.rickandmorty.presentation.characterlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.unit.dp
import com.dherediat97.rickandmorty.presentation.error.ErrorComposableView
import com.dherediat97.rickandmorty.presentation.loading.LoadingComposableView
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel


@Composable
fun CharacterListScreen(viewModel: CharacterListViewModel = koinViewModel(), onNavigateCharacter: (Int) -> Unit) {
    val data by viewModel.uiState.collectAsState()
    val listState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        viewModel.fetchCharactersFirstTime()
    }

    InfiniteLoading(listState, onLoadMore = {
        data.page++
        viewModel.fetchCharacterPaginated()
    })

    if (data.error && data.characters.isEmpty()) ErrorComposableView()


    if (!data.isLoading) {
        val items by remember { mutableStateOf(data.characters) }
        LazyVerticalGrid(state = listState, columns = GridCells.Adaptive(128.dp), contentPadding = PaddingValues(
            start = 8.dp,
            top = 16.dp,
            end = 8.dp,
            bottom = 16.dp
        ), content = {
            itemsIndexed(items = items, key = { _, character -> character.id }) { _, character ->
                CharacterCard(character) {
                    onNavigateCharacter(character.id)
                }
            }
        })
    } else {
        if (data.characters.isEmpty())
            LoadingComposableView()
    }
}

@Composable
fun InfiniteLoading(lazyListState: LazyGridState, buffer: Int = 5, onLoadMore: () -> Unit) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            lastVisibleItemIndex > (totalItems - buffer)
        }
    }
    LaunchedEffect(loadMore) {
        snapshotFlow {
            loadMore.value
        }.distinctUntilChanged().collect {
            onLoadMore()
        }
    }
}