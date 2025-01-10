package com.hraj9258.musicplayer.core.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hraj9258.musicplayer.music.presentation.SharedViewModel
import com.hraj9258.musicplayer.music.presentation.music_list.MusicListScreenRoot
import com.hraj9258.musicplayer.music.presentation.music_player.MusicPlayerScreenRoot
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveMusicListPlayerPane(
    sharedViewModel: SharedViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                MusicListScreenRoot(
                    onMusicClick = { musicUi ->
                        sharedViewModel.onSelectedMusic(musicUi)
                        navigator.navigateTo(
                            pane = ListDetailPaneScaffoldRole.Detail
                        )
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