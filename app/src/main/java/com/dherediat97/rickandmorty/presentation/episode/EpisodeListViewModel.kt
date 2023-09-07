package com.dherediat97.rickandmorty.presentation.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dherediat97.rickandmorty.domain.model.Episode
import com.dherediat97.rickandmorty.domain.model.ResponseGetAllEpisodes
import com.dherediat97.rickandmorty.domain.repository.EpisodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EpisodeListViewModel : ViewModel() {
    private val repository = EpisodeRepository()

    private val _state = MutableStateFlow(EpisodeListUiState())
    val uiState: StateFlow<EpisodeListUiState>
        get() = _state


    fun fetchEpisodes(episodes: List<String>) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {
                val responseGetAllEpisode = ResponseGetAllEpisodes(episodes)
                val episodeList = mutableListOf<String>()
                responseGetAllEpisode.episodes.forEach { episode ->
                    episodeList.add(episode.split("/").last())
                }
                val episodeListJoin = episodeList.joinToString(separator = ",")
                val episodesIdList = repository.getEpisodesByCharacterId(episodeListJoin)
                _state.update { it.copy(episodes = episodesIdList, isLoading = false) }
            }.onFailure {
                _state.update { it.copy(error = true, isLoading = false) }
            }
        }
    }

    data class EpisodeListUiState(
        val episodes: List<Episode> = emptyList(),
        val isLoading: Boolean = false,
        val error: Boolean = false,
    )
}