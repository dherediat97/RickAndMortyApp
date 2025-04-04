package com.dherediat97.rickandmorty.presentation.characterlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.model.ResponsePaginate
import com.dherediat97.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class CharacterListViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var page: Int = savedStateHandle["page"] ?: 1

    private val characterRepository = CharacterRepository()

    val characterList: Flow<ResponsePaginate<Character>> =
        characterRepository.getAllCharacters(page)

    fun loadMoreCharacters(page: Int): Flow<ResponsePaginate<Character>> =
        characterRepository.getAllCharacters(page)
}