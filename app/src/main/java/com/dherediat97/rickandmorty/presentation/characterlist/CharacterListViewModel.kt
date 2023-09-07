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

    private suspend fun fetchCharacters() {
        runCatching {
            _state.update { it.copy(isLoading = true) }
            val characters = repository.getAllCharacters(page = _state.value.page)
            _state.update { it.copy(characters = _state.value.characters + characters, isLoading = false) }
        }.onFailure {
            _state.update { it.copy(error = true) }
        }
    }

    fun fetchCharactersFirstTime() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_state.value.characters.isEmpty()) {
                fetchCharacters()
            }
        }
    }

    fun fetchCharacterPaginated() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCharacters()
        }
    }

    data class CharacterListUiState(
        val characters: List<Character> = emptyList(),
        val isLoading: Boolean = false,
        val error: Boolean = false,
        var page: Int = 1,
        var isEndReached: Boolean = false
    )
}