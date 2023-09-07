package com.dherediat97.rickandmorty.domain.model

data class ResponseGetAllCharacters(
    val info: Info,
    val results: List<Character>
)