package com.dherediat97.rickandmorty.domain.model

data class FilterCharacter(
    var byName: Boolean = false,
    var byStatus: Boolean = false,
    var bySpecies: Boolean = false,
    var byGender: Boolean = false
)