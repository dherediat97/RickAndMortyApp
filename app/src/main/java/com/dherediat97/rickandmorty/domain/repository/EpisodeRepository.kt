package com.dherediat97.rickandmorty.domain.repository

import com.dherediat97.rickandmorty.data.network.RetrofitInstance
import com.dherediat97.rickandmorty.domain.model.CharacterInfo
import com.dherediat97.rickandmorty.domain.model.Episode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EpisodeRepository {
    private val rickMortyApiService = RetrofitInstance.rickMortyApiService

    fun getEpisodesByCharacterId(character: CharacterInfo): Flow<List<Episode>> = flow {
        val responseEpisodes = rickMortyApiService.getEpisodes()
        val filterEpisodesByCharacterId =
            responseEpisodes.results.filter { it.characters.contains(character.episodes.first()) }
        emit(filterEpisodesByCharacterId)
    }

}