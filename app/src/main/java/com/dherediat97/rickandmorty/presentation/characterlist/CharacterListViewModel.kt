package com.dherediat97.rickandmorty.presentation.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterListViewModel : ViewModel() {
    private val repository = CharacterRepository()

    private val _state = MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState>
        get() = _state


    private val _searchState = MutableStateFlow(CharacterSearchUiState())
    val searchUiState: StateFlow<CharacterSearchUiState>
        get() = _searchState

    private suspend fun fetchCharacters(page: Int) {
        runCatching {
            _state.update { it.copy(isLoading = true) }
            val characters = repository.getAllCharacters(page = page)
            _state.update {
                it.copy(characters = if (page == 1) characters else _state.value.characters + characters, isLoading = false)
            }
        }.onFailure {
            _state.update { it.copy(error = true) }
        }
    }

    fun fetchCharactersFirstTime() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_state.value.characters.isEmpty()) {
                fetchCharacters(1)
            }
        }
    }

    fun fetchCharacterPaginated() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCharacters(_state.value.page)
        }
    }

    fun searchCharacter(name: String = "", specie: String = "", status: String = "", gender: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            _searchState.update { it.copy(isLoading = true) }
            runCatching {
                val characters = repository.searchCharacters(name, specie, status, gender)
                _searchState.update { it.copy(characters = characters) }
            }.onFailure {
                _searchState.update { it.copy(error = true, characters = emptyList()) }
            }
            _searchState.update { it.copy(isLoading = false) }
        }
    }


    data class CharacterListUiState(
        val characters: List<Character> = emptyList(),
        val isLoading: Boolean = false,
        val error: Boolean = false,
        var page: Int = 1,
        var isEndReached: Boolean = false
    )

    data class CharacterSearchUiState(
        var characters: List<Character> = emptyList(),
        val isLoading: Boolean = false,
        val error: Boolean = false,
        var isSearchbarActive: Boolean = false
    )
}