package com.dherediat97.rickandmorty.presentation.appbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.dherediat97.rickandmorty.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarView(
    title: String = stringResource(R.string.app_name),
    onBackAction: () -> Unit,
    onSearchAction: () -> Unit
) {
    val navController = rememberNavController()
    var isMainScreen by remember { mutableStateOf(true) }
    var screenTitle by remember { mutableStateOf(title) }

    LaunchedEffect(navController.currentBackStackEntryFlow) {
        navController.currentBackStackEntryFlow.collect {
//            isMainScreen = it.destination.route ?: false
            screenTitle = it.destination.route ?: title
            println(it.destination)
        }
    }


    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ), title = { Text(screenTitle) }, actions = {
            Icon(
                painter = rememberVectorPainter(image = Icons.Default.Search),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable(onClick = onSearchAction),
                contentDescription = "Search characters"
            )
        }, navigationIcon = {
//            if (isMainScreen) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.onSecondary,
//                    modifier = Modifier
//                        .padding(start = 8.dp)
//                        .clickable(onClick = onBackAction)
//                )
//            } else {
//
//
//            }

        })
}
