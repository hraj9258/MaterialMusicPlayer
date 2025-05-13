package com.hraj9258.musicplayer.navigation.domain

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.hraj9258.musicplayer.R

enum class AppDestinations(
    @StringRes val label: Int,
    val iconSelected: ImageVector,
    val iconUnSelected: ImageVector,
    @StringRes val contentDescription: Int
) {
    MusicListScreen(
        R.string.musicList,
        Icons.Filled.MusicNote,
        Icons.Outlined.MusicNote,
        R.string.musicList
    ),
    BrowseMusicScreen(
        R.string.browseMusic,
        Icons.Filled.Folder,
        Icons.Outlined.Folder,
        R.string.browseMusic
    ),
    SettingsScreen(
        R.string.settings,
        Icons.Filled.Settings,
        Icons.Outlined.Settings,
        R.string.settings
    )
}