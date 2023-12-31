package com.dherediat97.rickandmorty.presentation

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
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
import com.dherediat97.rickandmorty.ui.ScaleAndAlphaArgs
import com.dherediat97.rickandmorty.ui.calculateDelayAndEasing
import com.dherediat97.rickandmorty.ui.scaleAndAlpha
import com.dherediat97.rickandmorty.ui.theme.RickAndMortyAppTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.component.KoinComponent


class MainActivity : ComponentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickAndMortyAppTheme {
                MainContainer()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MainContainer(viewModel: CharacterListViewModel = koinViewModel()) {
    val navController: NavHostController = rememberNavController()
    var isParentView by remember { mutableStateOf(true) }
    var isSearchBarActive by remember { mutableStateOf(false) }
    var queryCharacter by remember { mutableStateOf("") }
    val data by viewModel.searchUiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyGridState()
    var filterCharacter: FilterCharacter
    val allFilters = listOf(FilterCharacterEnum.ByName.label,
        FilterCharacterEnum.ByStatus.label,
        FilterCharacterEnum.BySpecies.label,
        FilterCharacterEnum.ByGender.label
    )
    var title by remember { mutableStateOf("Rick & Morty") }


    LaunchedEffect(Unit) {
        viewModel.fetchCharactersFirstTime()
    }

    LaunchedEffect(navController.currentBackStackEntryFlow) {
        navController.currentBackStackEntryFlow.collect {
            val characterName: String? = it.arguments?.getString("characterName")
            isParentView = it.destination.route == "/"
            title = if (isParentView) "Rick & Morty" else "About $characterName"
        }
    }

    fun filterCharacter(selectedFilter: String) {
        viewModel.searchUiState.value.characters = emptyList()
        filterCharacter = FilterCharacter(byName = false, byStatus = false, bySpecies = false, byGender = false)
        when (selectedFilter) {
            FilterCharacterEnum.ByName.label -> filterCharacter.byName = true
            FilterCharacterEnum.ByStatus.label -> filterCharacter.byStatus = true
            FilterCharacterEnum.ByGender.label -> filterCharacter.byGender = true
            FilterCharacterEnum.BySpecies.label -> filterCharacter.bySpecies = true
        }
        if (!filterCharacter.byName) {
            viewModel.searchCharacter(
                "",
                if (filterCharacter.bySpecies) queryCharacter else "",
                if (filterCharacter.byStatus) queryCharacter else "",
                if (filterCharacter.byGender) queryCharacter else ""
            )
        } else {
            viewModel.searchCharacter(
                queryCharacter,
                "",
                "",
                ""
            )
        }

    }
    Scaffold(
        topBar = {
            if (isSearchBarActive) {
                var selectedFilter by remember { mutableStateOf("") }
                SearchBar(modifier = Modifier.fillMaxWidth(), query = queryCharacter, onQueryChange = {
                    queryCharacter = it
                    filterCharacter(selectedFilter)
                }, onSearch = {
                    queryCharacter = it
                    filterCharacter(selectedFilter)
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
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(allFilters) { filter ->
                            FilterChip(modifier = Modifier.padding(horizontal = 6.dp),
                                selected = (filter == selectedFilter), onClick = {
                                    selectedFilter = filter
                                    filterCharacter(selectedFilter)
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

                                            navController.navigate(
                                                "characterDetail/characterId=${character.id}&characterName=${character.name.trim()}") {
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
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    title = {
                        Text(title)
                    },
                    actions = {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Default.Search),
                            tint = MaterialTheme.colorScheme.onPrimary,
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
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable("/") {
            CharacterListScreen(
                onNavigateCharacter = { characterId, characterName ->
                    navController.navigate("characterDetail/characterId=$characterId&characterName=${characterName.trim()}") {
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
        composable("characterDetail/characterId={characterId}&characterName={characterName}",
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

@Preview(device = "spec:width=1080px,height=1920px,dpi=480", showSystemUi = true, showBackground = true)
@Composable
fun AppPreview() {
    MainContainer()
}
