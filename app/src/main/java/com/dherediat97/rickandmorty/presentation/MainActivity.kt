package com.dherediat97.rickandmorty.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dherediat97.rickandmorty.domain.model.FilterCharacter
import com.dherediat97.rickandmorty.domain.model.FilterCharacterEnum
import com.dherediat97.rickandmorty.presentation.characterdetail.CharacterDetailScreen
import com.dherediat97.rickandmorty.presentation.characterlist.CharacterCard
import com.dherediat97.rickandmorty.presentation.characterlist.CharacterListScreen
import com.dherediat97.rickandmorty.presentation.characterlist.CharacterListViewModel
import com.dherediat97.rickandmorty.presentation.loading.LoadingComposableView
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
    var isParentView by remember { mutableStateOf(true) }
    var isSearchBarActive by remember { mutableStateOf(false) }
    var queryCharacter by remember { mutableStateOf("") }
    val data by viewModel.searchUiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyGridState()
    var filterCharacter = FilterCharacter()
    val allFilters = listOf(FilterCharacterEnum.ByName.label,
        FilterCharacterEnum.ByStatus.label,
        FilterCharacterEnum.BySpecies.label,
        FilterCharacterEnum.ByGender.label
    )


    LaunchedEffect(Unit) {
        viewModel.fetchCharactersFirstTime()
    }

    LaunchedEffect(navController.currentBackStackEntryFlow) {
        navController.currentBackStackEntryFlow.collect {
            isParentView = it.destination.route == "/"
        }
    }
    Scaffold(
        topBar = {
            if (isSearchBarActive) {
                SearchBar(modifier = Modifier.fillMaxWidth(), query = queryCharacter, onQueryChange = {
                    queryCharacter = it
                    viewModel.searchCharacter(
                        if (filterCharacter.byName) queryCharacter else "",
                        if (filterCharacter.bySpecies) queryCharacter else "",
                        if (filterCharacter.byStatus) queryCharacter else "",
                        if (filterCharacter.byGender) queryCharacter else ""
                    )
                }, onSearch = {
                    queryCharacter = it
                    viewModel.searchCharacter(
                        if (filterCharacter.byName) queryCharacter else "",
                        if (filterCharacter.bySpecies) queryCharacter else "",
                        if (filterCharacter.byStatus) queryCharacter else "",
                        if (filterCharacter.byGender) queryCharacter else "")
                }, active = isSearchBarActive, onActiveChange = {
                    isSearchBarActive = it
                }, placeholder = {
                    Text("Search your favorite character...")
                }, leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                }, trailingIcon = {
                    if (isSearchBarActive) Icon(imageVector = Icons.Default.Clear, modifier = Modifier.clickable {
                        if (queryCharacter.isNotEmpty()) {
                            queryCharacter = ""
                        } else {
                            isSearchBarActive = false
                        }
                        data.characters = emptyList()
                    }, contentDescription = "Clear search query character")
                }) {
                    var selectedItem by remember { mutableStateOf(allFilters[0]) }
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(allFilters) { filter ->
                            FilterChip(modifier = Modifier.padding(horizontal = 6.dp),
                                selected = (filter == selectedItem), onClick = {
                                    selectedItem = filter
                                    filterCharacter = FilterCharacter()
                                    when (selectedItem) {
                                        FilterCharacterEnum.ByName.label -> filterCharacter.byName = true
                                        FilterCharacterEnum.ByStatus.label -> filterCharacter.byStatus = true
                                        FilterCharacterEnum.ByGender.label -> filterCharacter.byGender = true
                                        FilterCharacterEnum.BySpecies.label -> filterCharacter.bySpecies = true
                                    }
                                    viewModel.searchCharacter(
                                        if (filterCharacter.byName) queryCharacter else "",
                                        if (filterCharacter.bySpecies) queryCharacter else "",
                                        if (filterCharacter.byStatus) queryCharacter else "",
                                        if (filterCharacter.byGender) queryCharacter else "")
                                }, label = { Text(filter, fontSize = 10.sp) }, leadingIcon = {
                                    Icon(imageVector = when (filter) {
                                        FilterCharacterEnum.ByName.label -> Icons.Default.Abc
                                        FilterCharacterEnum.ByStatus.label -> Icons.Default.Person
                                        FilterCharacterEnum.ByGender.label -> Icons.Default.Transgender
                                        FilterCharacterEnum.BySpecies.label -> Icons.Default.DarkMode
                                        else -> {
                                            Icons.Default.DeviceUnknown
                                        }
                                    }, contentDescription = filter)
                                })
                        }
                    }
                    if (!data.isLoading) {
                        if (queryCharacter.isNotEmpty() && data.characters.isNotEmpty()) {
                            val items by remember { mutableStateOf(data.characters) }
                            LazyVerticalGrid(state = listState, columns = GridCells.Adaptive(128.dp),
                                contentPadding = PaddingValues(
                                    start = 8.dp,
                                    top = 8.dp,
                                    end = 8.dp,
                                    bottom = 8.dp
                                ), content = {
                                    itemsIndexed(items = items, key = { _, character -> character.id }) { index, character ->
                                        val (delay, easing) = listState.calculateDelayAndEasing(index, 1)
                                        val animation = tween<Float>(durationMillis = 50, delayMillis = delay, easing = easing)
                                        val args = ScaleAndAlphaArgs(fromScale = 2f, toScale = 1f, fromAlpha = 0f, toAlpha = 1f)
                                        val (scale, alpha) = scaleAndAlpha(args = args, animation = animation)
                                        CharacterCard(
                                            modifier = Modifier.graphicsLayer(alpha = alpha, scaleX = scale, scaleY = scale),
                                            character) {
                                            keyboardController?.hide()

                                            navController.navigate("characterDetail/${character.id}") {
                                                isSearchBarActive = false
                                                queryCharacter = ""
                                                data.characters = emptyList()
                                                navController.graph.startDestinationRoute?.let { route ->
                                                    popUpTo(route) {
                                                        saveState = true
                                                    }
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                                })
                        }
                    } else {
                        LoadingComposableView()
                    }
                }
            } else
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
                                    isSearchBarActive = !isSearchBarActive
                                },
                            contentDescription = "Search characters")
                    },
                    navigationIcon = {
                        if (!isParentView) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .clickable { navController.navigateUp() })
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
                    navController.navigate("characterDetail/$characterId") {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
        composable("characterDetail/{characterId}",
            arguments = listOf(
                navArgument("characterId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt("characterId")?.let { CharacterDetailScreen(it) }
        }
    }
}
