package com.dherediat97.rickandmorty.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    val id: Int,
    val name: String,
    val episode: String,
    val characters: List<String>
)
