package com.hraj9258.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hraj9258.musicplayer.navigation.MainNavigationSuitScaffold
import com.hraj9258.musicplayer.core.presentation.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicPlayerTheme {
                MainNavigationSuitScaffold()
            }
        }
    }
}