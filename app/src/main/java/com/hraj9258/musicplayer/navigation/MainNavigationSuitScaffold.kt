package com.hraj9258.musicplayer.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hraj9258.musicplayer.music.presentation.NotYetImplemented

@Composable
fun MainNavigationSuitScaffold(
    modifier: Modifier = Modifier
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.MusicListScreen) }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            if (it == currentDestination) it.iconSelected else it.iconUnSelected,
                            contentDescription = stringResource(it.contentDescription)
                        )
                    },
                    label = { Text(stringResource(it.label)) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
    ) {
        Scaffold { innerPadding ->
            when (currentDestination) {
                AppDestinations.MusicListScreen -> {
                    AdaptiveMusicListPlayerPane(
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
                AppDestinations.BrowseMusicScreen -> {
                    NotYetImplemented()
                }
                AppDestinations.SettingsScreen -> {
                    NotYetImplemented()
                }
            }
        }

    }
}