package com.dherediat97.rickandmorty.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val name: String,
    val url: String
) {
    constructor() : this("", "")
}
