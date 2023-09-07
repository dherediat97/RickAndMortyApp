package com.dherediat97.rickandmorty.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dherediat97.rickandmorty.presentation.characterdetail.CharacterDetailScreen
import com.dherediat97.rickandmorty.presentation.characterlist.CharacterCard
import com.dherediat97.rickandmorty.presentation.characterlist.CharacterListScreen
import com.dherediat97.rickandmorty.presentation.characterlist.CharacterListViewModel
import com.dherediat97.rickandmorty.ui.theme.PruebaTecnicaZaraTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.component.KoinComponent


class MainActivity : ComponentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PruebaTecnicaZaraTheme {
                ScaffoldPage()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ScaffoldPage(viewModel: CharacterListViewModel = koinViewModel()) {
    val navController: NavHostController = rememberNavController()
    var parent by remember { mutableStateOf(true) }
    var active by remember { mutableStateOf(false) }
    var queryCharacter by remember { mutableStateOf("") }
    val data by viewModel.searchUiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.fetchCharactersFirstTime()
    }

    LaunchedEffect(navController.currentBackStackEntryFlow) {
        navController.currentBackStackEntryFlow.collect {
            parent = it.destination.route == "/"
        }
    }
    Scaffold(
        topBar = {
            if (active)
                SearchBar(modifier = Modifier.fillMaxWidth(), query = queryCharacter, onQueryChange = {
                    queryCharacter = it
                    viewModel.searchCharacter(queryCharacter)
                }, onSearch = {
                    queryCharacter = it
                    viewModel.searchCharacter(queryCharacter)
                }, active = active, onActiveChange = {
                    active = it
                }, placeholder = {
                    Text("Search your favorite character...")
                }, leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                }, trailingIcon = {
                    if (active) Icon(imageVector = Icons.Default.Clear, modifier = Modifier.clickable {
                        if (queryCharacter.isNotEmpty()) {
                            queryCharacter = ""
                            data.characters = emptyList()
                        } else {
                            active = false
                            data.characters = emptyList()
                        }
                    }, contentDescription = "")
                }) {
                    if (data.characters.isNotEmpty()) {
                        val items by remember { mutableStateOf(data.characters) }
                        LazyVerticalGrid(columns = GridCells.Adaptive(128.dp), contentPadding = PaddingValues(
                            start = 8.dp,
                            top = 16.dp,
                            end = 8.dp,
                            bottom = 16.dp
                        ), content = {
                            itemsIndexed(items = items, key = { _, character -> character.id }) { _, character ->
                                CharacterCard(character) {
                                    keyboardController?.hide()
                                    active = false
                                    queryCharacter = ""
                                    data.characters = emptyList()
                                    navController.navigate("characterDetail/${character.id}")
                                }
                            }
                        })
                    }
                }
            else
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                    title = {
                        Text("Rick & Morty")
                    },
                    actions = {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Default.Search),
                            tint = Color.Black,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable {
                                    active = !active
                                },
                            contentDescription = "Filter characters")
                    },
                    navigationIcon = {
                        if (!parent) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .clickable {
                                        navController.navigateUp()
                                    })
                        }
                    }
                )
        },
    ) { innerPadding ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding), color = MaterialTheme.colorScheme.background) {
            NavHost(navController)
        }
    }

}

@Composable
fun NavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "/"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("/") {
            CharacterListScreen(
                onNavigateCharacter = { characterId ->
                    navController.navigate("characterDetail/$characterId")
                },
            )
        }
        composable("characterDetail/{characterId}", arguments = listOf(
            navArgument("characterId") {
                type = NavType.IntType
            }
        )
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt("characterId")?.let { CharacterDetailScreen(it) }
        }
    }
}
