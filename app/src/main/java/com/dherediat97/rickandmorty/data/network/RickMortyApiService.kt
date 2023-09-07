package com.dherediat97.rickandmorty.data.network

import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.model.Episode
import com.dherediat97.rickandmorty.domain.model.ResponseGetAllCharacters
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyApiService {
    @GET("character")
    suspend fun getAllCharacters(@Query("page")
                                 page: Int): ResponseGetAllCharacters

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Character

    @GET("character")
    suspend fun searchCharacter(@Query("name") name: String): ResponseGetAllCharacters

    @GET("episode/{episodes}")
    suspend fun getEpisodes(@Path("episodes") episodes: String): List<Episode>

    @GET("episode/{episode}")
    suspend fun getEpisode(@Path("episode") episode: String): Episode
}