package com.dherediat97.rickandmorty.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dherediat97.rickandmorty.presentation.characterdetail.CharacterDetailScreen
import com.dherediat97.rickandmorty.presentation.characterlist.CharacterListScreen
import com.dherediat97.rickandmorty.ui.theme.PruebaTecnicaZaraTheme
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldPage() {
    val navController: NavHostController = rememberNavController()
    var parent by remember { mutableStateOf(true) }
    LaunchedEffect(navController.currentBackStackEntryFlow) {
        navController.currentBackStackEntryFlow.collect {
            parent = it.destination.route == "/"
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary,
                ),
                title = {
                    Text("Rick & Morty")
                },
                navigationIcon = {
                    if (!parent) {
                        IconButton(onClick = {
                            navController.navigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null,
                                tint= MaterialTheme.colorScheme.onSecondary
                            )
                        }
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
