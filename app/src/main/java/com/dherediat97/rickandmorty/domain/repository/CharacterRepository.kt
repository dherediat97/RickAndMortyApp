package com.dherediat97.rickandmorty.domain.repository

import com.dherediat97.rickandmorty.data.network.RetrofitInstance
import com.dherediat97.rickandmorty.domain.model.Character

class CharacterRepository {
    private val rickMortyApiService = RetrofitInstance.rickMortyApiService

    suspend fun getAllCharacters(page: Int): List<Character> {
        val responseGetAllCharacters = rickMortyApiService.getAllCharacters(page)
        return responseGetAllCharacters.results
    }

    suspend fun searchCharacters(name: String, specie: String, status: String, gender: String): List<Character> {
        val responseSearchCharacters = rickMortyApiService.searchCharacter(name, specie, status, gender)
        return responseSearchCharacters.results
    }

    suspend fun getCharacter(id: Int): Character {
        return rickMortyApiService.getCharacter(id)
    }

}