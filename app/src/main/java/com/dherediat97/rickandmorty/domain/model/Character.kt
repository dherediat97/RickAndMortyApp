package com.dherediat97.rickandmorty.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: Int,
    val name: String,
    val image: String,
    val status: String,
) {
    constructor() : this(0, "", "",CharacterStatus.unknown.name)
}

@Serializable
data class CharacterInfo(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val origin: Location,
    val location: Location,
    val episodes: List<String>
) {
    constructor() : this(
        0,
        "",
        CharacterStatus.unknown.name,
        "",
        "",
        "",
        "",
        Location(),
        Location(),
        emptyList()
    )
}

