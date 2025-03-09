package com.dherediat97.rickandmorty.presentation.characterlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.model.ResponseGetAll
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

@Composable
fun CharacterListView(
    characterListViewModel: CharacterListViewModel,
    onNavigateCharacter: (Character) -> Unit
) {
    val responseGetAllCharacters: ResponseGetAll<Character> by characterListViewModel.characterList.collectAsStateWithLifecycle(
        initialValue = ResponseGetAll(),
    )
    val characterList = responseGetAllCharacters.results

    val listState = rememberLazyGridState()

    InfiniteLoading(lazyListState = listState, onLoadMore = {
        flow {
            characterListViewModel.page++
            val responseData = characterListViewModel.loadMoreCharacters()
            emit(responseData)
        }
    })

    LazyVerticalGrid(state = listState, columns = GridCells.Adaptive(120.dp), content = {
        itemsIndexed(
            items = characterList, key = { _, character -> character.id }) { _, character ->

            CharacterCard(
                modifier = Modifier.clickable(onClick = {
                    onNavigateCharacter(character)
                }),
                character = character,
            )
        }
    })
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
        snapshotFlow(loadMore::value).distinctUntilChanged().collect { onLoadMore() }
    }
}
