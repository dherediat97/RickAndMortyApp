package com.dherediat97.rickandmorty.domain.model

data class ResponseGetAllEpisodes(
   val episodes: List<String> = mutableListOf()
)