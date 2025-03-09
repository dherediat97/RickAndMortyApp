package com.dherediat97.rickandmorty.app

enum class Screen{
    HOME,
    CHARACTER_DETAIL,
    CHARACTER_SEARCH,
    EPISODE_DETAIL,
}

sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(Screen.HOME.name)
    data object CharacterDetail : NavigationItem(Screen.CHARACTER_DETAIL.name)
    data object SearchCharacter : NavigationItem(Screen.CHARACTER_SEARCH.name)
    data object EpisodeDetail : NavigationItem(Screen.EPISODE_DETAIL.name)
}
