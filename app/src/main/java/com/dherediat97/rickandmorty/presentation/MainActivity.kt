package com.dherediat97.rickandmorty.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.dherediat97.rickandmorty.presentation.mainview.MainView
import com.dherediat97.rickandmorty.ui.theme.RickAndMortyAppTheme
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickAndMortyAppTheme {
                MainView(navHostController = rememberNavController())
            }
        }
    }
}