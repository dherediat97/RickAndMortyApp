package com.dherediat97.rickandmorty.domain.repository

import com.dherediat97.rickandmorty.data.network.RetrofitInstance
import com.dherediat97.rickandmorty.domain.model.Episode

class EpisodeRepository {
    private val rickMortyApiService = RetrofitInstance.rickMortyApiService

    suspend fun getEpisodesByCharacterId(episodes: String): MutableList<Episode> {
        val episodesList = mutableListOf<Episode>()
        if (!episodes.contains(","))
            episodesList.add(rickMortyApiService.getEpisode(episode = episodes))
        else
            episodesList.addAll(rickMortyApiService.getEpisodes(episodes = episodes))
        return episodesList
    }

}