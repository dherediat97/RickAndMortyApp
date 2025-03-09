package com.dherediat97.rickandmorty.presentation.charactersearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterSearchViewModel : ViewModel() {
    private val repository = CharacterRepository()

    private val _state = MutableStateFlow(CharacterSearchUiState())
    val uiState: StateFlow<CharacterSearchUiState>
        get() = _state


    fun searchCharacter(
        name: String = "",
        specie: String = "",
        status: String = "",
        gender: String = ""
    ) {
        if (name.isEmpty() || specie.isEmpty() || status.isEmpty() || gender.isEmpty()) {
            _state.update {
                it.copy(characters = _state.value.characters)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            runCatching {
                val characters = repository.searchCharacters(name, specie, status, gender)
                _state.update { it.copy(characters = characters) }
            }.onFailure {
                _state.update { it.copy(error = true, characters = emptyList()) }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    data class CharacterSearchUiState(
        var characters: List<Character> = emptyList(),
        val isLoading: Boolean = false,
        val error: Boolean = false,
        var isSearchbarActive: Boolean = false
    )
}