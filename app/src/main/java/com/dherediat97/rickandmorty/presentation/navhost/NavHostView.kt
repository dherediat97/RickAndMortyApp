package com.dherediat97.rickandmorty.presentation.navhost

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dherediat97.rickandmorty.app.NavigationItem
import com.dherediat97.rickandmorty.domain.model.Character
import com.dherediat97.rickandmorty.domain.model.Episode
import com.dherediat97.rickandmorty.presentation.characterdetail.CharacterDetailView
import com.dherediat97.rickandmorty.presentation.characterlist.CharacterListView
import com.dherediat97.rickandmorty.presentation.charactersearch.CharacterSearchView
import com.dherediat97.rickandmorty.presentation.episodedetail.EpisodeDetailView
import org.koin.androidx.compose.koinViewModel


@Composable
fun NavHostView(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Home.route,
    ) {

        composable(NavigationItem.Home.route) {
            CharacterListView(
                characterListViewModel = koinViewModel(),
                onNavigateCharacter = { character ->
                    navController.navigate(
                        Character(
                            id = character.id,
                            image = character.image,
                            name = character.name,
                        )
                    )
                },
            )
        }

        composable<Character> {
            CharacterDetailView(
                characterDetailViewModel = koinViewModel()
            )
        }

        composable<Episode> { navStackEntry ->
            EpisodeDetailView(
                episodeId = navStackEntry.toRoute(),
                episodeDetailViewModel = koinViewModel()
            )
        }

        composable(NavigationItem.SearchCharacter.route) {
            CharacterSearchView(
                searchViewModel = koinViewModel(),
                onNavigateCharacter = { character ->
                    navController.navigate("${NavigationItem.CharacterDetail.route}/$character")
                },
            )
        }
    }
}