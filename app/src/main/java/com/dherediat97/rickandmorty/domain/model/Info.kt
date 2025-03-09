package com.dherediat97.rickandmorty.domain.model

data class Info(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String
) {
    constructor() : this(0, 0, "", "")
}