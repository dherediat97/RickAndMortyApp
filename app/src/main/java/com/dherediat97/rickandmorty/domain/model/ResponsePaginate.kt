package com.dherediat97.rickandmorty.domain.model

data class ResponsePaginate<T>(
    val info: Info,
    val results: List<T>
) {
    constructor() : this(Info(), emptyList())
}