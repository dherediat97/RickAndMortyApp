package com.dherediat97.rickandmorty.presentation.characterdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterDetailViewModel : ViewModel() {

    private val repository = CharacterRepository()

    private val _state = MutableStateFlow(CharacterUiState())
    val uiState: StateFlow<CharacterUiState>
        get() = _state

    fun fetchCharacter(id: Int) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {
                val character = repository.getCharacter(id)
                _state.update { it.copy(character = character) }
            }.onFailure {
                _state.update { it.copy(error = true) }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    data class CharacterUiState(
        val character: Character? = null,
        val isLoading: Boolean = false,
        val error: Boolean = false,
    )

}