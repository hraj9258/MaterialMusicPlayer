package com.hraj9258.musicplayer.music.presentation.music_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hraj9258.musicplayer.music.presentation.SharedState
import com.hraj9258.musicplayer.music.presentation.SharedViewModel
import com.hraj9258.musicplayer.music.presentation.models.MusicUI
import com.hraj9258.musicplayer.music.presentation.music_list.components.MusicListItem
import com.hraj9258.musicplayer.music.presentation.music_list.components.previewMusicUI
import com.hraj9258.musicplayer.core.presentation.ui.theme.MusicPlayerTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MusicListScreenRoot(
    viewModel: SharedViewModel = koinViewModel(),
    onMusicClick: (MusicUI) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MusicListScreen(
        state = state,
        onMusicListAction = {musicListAction->
            when(musicListAction){
                is MusicListAction.OnMusicListClick -> onMusicClick(musicListAction.musicUi)
                else -> Unit
            }
//            viewModel.onAction(musicListAction)
        }
    )
}

@Composable
fun MusicListScreen(
    state: SharedState,
    onMusicListAction: (MusicListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.musicList) { songUi ->
                MusicListItem(
                    musicUi = songUi,
                    onClick = { onMusicListAction(MusicListAction.OnMusicListClick(songUi)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun SongListPreview() {
    MusicPlayerTheme {
        MusicListScreen(
            state = SharedState(
                isLoading = false,
                musicList = (1..100).map {
                    previewMusicUI.copy(id = it.toLong())
                }
            ),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            onMusicListAction = {}
        )
    }
}