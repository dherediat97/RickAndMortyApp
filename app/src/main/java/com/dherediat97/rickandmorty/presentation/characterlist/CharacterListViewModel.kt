package com.dherediat97.rickandmorty.presentation.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dherediat97.rickandmorty.domain.repository.CharacterRepository
import com.dherediat97.rickandmorty.domain.model.Character
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterListViewModel : ViewModel() {
    private val repository = CharacterRepository()

    private val _state = MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState>
        get() = _state


    fun fetchCharacters(page: Int) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {
                val characters = repository.getAllCharacters(page)
                delay(250)
                _state.update { it.copy(characters = characters, isLoading = false) }
            }.onFailure {
                _state.update { it.copy(error = true, isLoading = false) }
            }
        }
    }

    data class CharacterListUiState(
        val characters: List<Character> = emptyList(),
        val isLoading: Boolean = false,
        val error: Boolean = false,
    )
}