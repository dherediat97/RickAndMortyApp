package com.dherediat97.rickandmorty.presentation.mainview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.dherediat97.rickandmorty.app.NavigationItem
import com.dherediat97.rickandmorty.presentation.appbar.TopAppBarView
import com.dherediat97.rickandmorty.presentation.navhost.NavHostView

@Composable
fun MainView(navHostController: NavHostController) {
    Scaffold(topBar = {
        TopAppBarView(onBackAction = {
            navHostController.navigate(NavigationItem.Home.route) {
            }
        }) {
            navHostController.navigate(NavigationItem.SearchCharacter.route)
        }
    }) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHostView(navHostController)
        }
    }
}