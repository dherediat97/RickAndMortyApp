package com.dherediat97.rickandmorty.presentation.episodelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.dherediat97.rickandmorty.domain.model.CharacterInfo
import com.dherediat97.rickandmorty.domain.model.Episode
import com.dherediat97.rickandmorty.domain.repository.EpisodeRepository
import kotlinx.coroutines.flow.Flow

class EpisodeListViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    val character = savedStateHandle.toRoute<CharacterInfo>()

    private val episodeRepository = EpisodeRepository()

    val episodeList: Flow<List<Episode>> = episodeRepository.getEpisodesByCharacterId(character)
}