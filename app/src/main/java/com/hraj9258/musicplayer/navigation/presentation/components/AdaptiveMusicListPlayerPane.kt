package com.hraj9258.musicplayer.navigation.presentation.components

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.hraj9258.musicplayer.music.presentation.SharedViewModel
import com.hraj9258.musicplayer.music.presentation.music_list.MusicListScreenRoot
import com.hraj9258.musicplayer.music.presentation.music_player.MusicPlayerScreenRoot
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveMusicListPlayerPane(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                MusicListScreenRoot(
                    onMusicClick = { musicUi ->
                        sharedViewModel.onSelectedMusic(musicUi)
                        coroutineScope.launch {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail
                            )
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                MusicPlayerScreenRoot(
                    viewModel = sharedViewModel,
                    onPlayPause = {
                        sharedViewModel.musicPlayPause()
                    },
                    onSeekToNextClick = {
                        sharedViewModel.seekToNextMediaItem()
                    },
                    onSeekToPreviousClick = {
                        sharedViewModel.seekToPreviousMediaItem()
                    },
                )
            }
        },
        modifier = modifier
    )


}