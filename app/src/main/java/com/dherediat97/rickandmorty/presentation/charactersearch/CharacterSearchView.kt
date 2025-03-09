package com.dherediat97.rickandmorty.presentation.charactersearch

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.model.FilterCharacter
import com.dherediat97.rickandmorty.domain.model.FilterCharacterEnum

@Composable
fun CharacterSearchView(
    searchViewModel: CharacterSearchViewModel,
    onNavigateCharacter: (Character) -> Unit
) {

    val data by searchViewModel.uiState.collectAsState()
    var isExpanded by remember { mutableStateOf(false) }
    val listState = rememberLazyGridState()
    var queryCharacter by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("") }
    var filterCharacter: FilterCharacter
    val allFilters = listOf(
        FilterCharacterEnum.ByName.label,
        FilterCharacterEnum.ByStatus.label,
        FilterCharacterEnum.BySpecies.label,
        FilterCharacterEnum.ByGender.label
    )

    fun changeFilter(selectedFilter: String) {
        searchViewModel.uiState.value.characters = emptyList()
        filterCharacter = FilterCharacter()
        when (selectedFilter) {
            FilterCharacterEnum.ByName.label -> filterCharacter.byName = true
            FilterCharacterEnum.ByStatus.label -> filterCharacter.byStatus = true
            FilterCharacterEnum.ByGender.label -> filterCharacter.byGender = true
            FilterCharacterEnum.BySpecies.label -> filterCharacter.bySpecies = true
        }
        if (!filterCharacter.byName) {
            searchViewModel.searchCharacter(
                "",
                if (filterCharacter.bySpecies) queryCharacter else "",
                if (filterCharacter.byStatus) queryCharacter else "",
                if (filterCharacter.byGender) queryCharacter else ""
            )
        } else {
            searchViewModel.searchCharacter(
                queryCharacter, "", "", ""
            )
        }
    }


//    Scaffold(topBar = {
//        SearchBar(
//            expanded = isExpanded,
//            onExpandedChange = { isExpanded = it },
//            content = {},
//            inputField = {
//                SearchBarDefaults.InputField(
//                    query = queryCharacter,
//                    onQueryChange = {
//                        queryCharacter = it
//                        changeFilter(selectedFilter)
//                    },
//                    onSearch = {
//                        isExpanded = false
//                        changeFilter(selectedFilter)
//                    },
//                    expanded = isExpanded,
//                    onExpandedChange = {
//                        isExpanded = it
//                    },
//                    placeholder = { Text("Search your favorite character...") },
//                    leadingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.Search,
//                            contentDescription = "Search Icon"
//                        )
//                    },
//                    trailingIcon = {
//                        if (isExpanded) Icon(
//                            imageVector = Icons.Default.Clear, modifier = Modifier.clickable {
//                                if (queryCharacter.isNotEmpty()) {
//                                    queryCharacter = ""
//                                } else {
//                                    isExpanded = false
//                                }
//                                data.characters = emptyList()
//                            }, contentDescription = "Clear search query character"
//                        )
//                    },
//                )
//            })
//    }, content = { _ ->
//        LazyRow(
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            items(allFilters) { filter ->
//                FilterChip(
//                    modifier = Modifier.padding(horizontal = 6.dp),
//                    selected = (filter == selectedFilter),
//                    onClick = {
//                        selectedFilter = filter
//                        changeFilter(selectedFilter)
//                    },
//                    label = { Text(filter, fontSize = 10.sp) },
//                    leadingIcon = {
//                        Icon(
//                            imageVector = when (filter) {
//                                FilterCharacterEnum.ByName.label -> Icons.Default.Abc
//                                FilterCharacterEnum.ByStatus.label -> Icons.Default.Person
//                                FilterCharacterEnum.ByGender.label -> Icons.Default.Transgender
//                                FilterCharacterEnum.BySpecies.label -> Icons.Default.DarkMode
//                                else -> Icons.Default.DeviceUnknown
//                            }, contentDescription = filter
//                        )
//                    })
//            }
//        }
//        if (data.isLoading) LoadingComposableView()
//        else if (queryCharacter.isNotEmpty() && data.characters.isNotEmpty()) {
//            val items by remember { mutableStateOf(data.characters) }
//            LazyVerticalGrid(
//                state = listState,
//                columns = GridCells.Adaptive(128.dp),
//
//                content = {
//                    itemsIndexed(
//                        items = items,
//                        key = { _, character -> character.id }) { index, character ->
//                        CharacterCard(
//                            modifier = Modifier.clickable(onClick = { onNavigateCharacter(character) }),
//                            character,
//                        )
//                    }
//                })
//        }
//    })
}

