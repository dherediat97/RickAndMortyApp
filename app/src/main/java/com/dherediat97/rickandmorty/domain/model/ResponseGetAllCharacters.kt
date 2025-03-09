package com.dherediat97.rickandmorty.domain.model

data class ResponseGetAll<T>(
    val info: Info,
    val results: List<T>
) {
    constructor() : this(Info(), emptyList())
}