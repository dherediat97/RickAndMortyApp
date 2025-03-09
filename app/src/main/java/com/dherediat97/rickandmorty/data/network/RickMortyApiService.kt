package com.dherediat97.rickandmorty.data.network

import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.model.CharacterInfo
import com.dherediat97.rickandmorty.domain.model.Episode
import com.dherediat97.rickandmorty.domain.model.ResponseGetAll
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyApiService {
    @GET("character")
    suspend fun getAllCharacters(
        @Query("page")
        page: Int
    ): ResponseGetAll<Character>

    @GET("character/{id}")
    suspend fun getCharacterInfo(
        @Path("id")
        id: Int
    ): CharacterInfo

    @GET("character")
    suspend fun searchCharacter(
        @Query("name")
        name: String,
        @Query("species")
        species: String,
        @Query("status")
        status: String,
        @Query("gender")
        gender: String,
    ): ResponseGetAll<Character>

    @GET("episode")
    suspend fun getEpisodes(): ResponseGetAll<Episode>
}