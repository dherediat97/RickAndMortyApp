package com.dherediat97.rickandmorty.domain.repository

import com.dherediat97.rickandmorty.data.network.RetrofitInstance
import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.model.CharacterInfo
import com.dherediat97.rickandmorty.domain.model.ResponseGetAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CharacterRepository {
    private val rickMortyApiService = RetrofitInstance.rickMortyApiService

    fun getAllCharacters(page: Int): Flow<ResponseGetAll<Character>> = flow {
        emit(rickMortyApiService.getAllCharacters(page))
    }

    suspend fun searchCharacters(
        name: String,
        specie: String,
        status: String,
        gender: String
    ): List<Character> {
        val responseSearchCharacters =
            rickMortyApiService.searchCharacter(name, specie, status, gender)
        return responseSearchCharacters.results
    }

    fun getCharacterInfo(id: Int): Flow<CharacterInfo> = flow {
        emit(rickMortyApiService.getCharacterInfo(id))
    }

}