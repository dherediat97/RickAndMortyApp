package com.dherediat97.rickandmorty.presentation.characterdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.model.CharacterInfo
import com.dherediat97.rickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val character = savedStateHandle.toRoute<Character>()

    private val characterInfoRepository = CharacterRepository()

    val characterInfo: Flow<CharacterInfo> = characterInfoRepository.getCharacterInfo(character.id)
}