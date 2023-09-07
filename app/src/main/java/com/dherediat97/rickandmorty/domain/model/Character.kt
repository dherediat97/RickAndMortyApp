package com.dherediat97.rickandmorty.domain.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val origin: Location,
    val location: Location,
    val episode: List<String>
)
