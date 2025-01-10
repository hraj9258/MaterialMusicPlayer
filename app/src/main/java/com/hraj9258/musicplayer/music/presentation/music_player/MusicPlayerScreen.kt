package com.hraj9258.musicplayer.music.presentation.music_player

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.twotone.MusicNote
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.hraj9258.musicplayer.music.presentation.SharedState
import com.hraj9258.musicplayer.music.presentation.SharedViewModel
import com.hraj9258.musicplayer.music.presentation.models.MusicUI
import com.hraj9258.musicplayer.core.presentation.ui.theme.MusicPlayerTheme
import com.hraj9258.musicplayer.core.presentation.ui.theme.balooBhai
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun MusicPlayerScreenRoot(
    viewModel: SharedViewModel = koinViewModel(),
    onPlayPause: () -> Unit,
    onSeekToNextClick: () -> Unit,
    onSeekToPreviousClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MusicPlayerScreen(
        state = state,
        onPlayPause = onPlayPause,
        onSeekToNextClick = onSeekToNextClick,
        onSeekToPreviousClick = onSeekToPreviousClick,
        mediaController = viewModel.mediaController
    )
}

@Composable
fun MusicPlayerScreen(
    state: SharedState,
    onPlayPause: () -> Unit,
    onSeekToNextClick: () -> Unit,
    onSeekToPreviousClick: () -> Unit,
    mediaController: MediaController? = null,
    modifier: Modifier = Modifier
) {
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }

    LaunchedEffect(mediaController) {
        mediaController?.let { controller ->
            duration = controller.duration.coerceAtLeast(0L)

            controller.addListener(object : Player.Listener {
                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    currentPosition = newPosition.positionMs.coerceAtLeast(0L)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        duration = controller.duration.coerceAtLeast(0L)
                    }
                }

            })
        }
    }

    LaunchedEffect(state.isPlaying) {
        while (state.isPlaying) {
            currentPosition = mediaController?.currentPosition?.coerceAtLeast(0L) ?: 0L
            delay(1000L)
        }
    }

    if (state.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (state.selectedMusic != null) {
        val selectedMusic = state.selectedMusic

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .size(300.dp)
                    .aspectRatio(4 / 3F),
                shape = CircleShape.copy(all = CornerSize(20.dp))
            ) {
                if (selectedMusic.albumArt != null) {
                    Image(
                        bitmap = selectedMusic.albumArt,
                        contentDescription = selectedMusic.title.toString(),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally),
                        imageVector = Icons.TwoTone.MusicNote,
                        contentDescription = "Music Note"
                    )
                }
            }
            Text(
                text = selectedMusic.title,
                fontFamily = balooBhai,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.basicMarquee()
            )
            Text(
                text = selectedMusic.artist,
                fontFamily = balooBhai,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )

            // Slider
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    formatTime(currentPosition)
                )
                Slider(
                    value = currentPosition.toFloat(),
                    onValueChange = { newValue ->
                        currentPosition = newValue.toLong()
                        mediaController?.seekTo(newValue.toLong())
                    },
                    valueRange = 0f..duration.toFloat(),
                    modifier = Modifier
                        .weight(1F)
                )
                Text(
                    text = formatTime(duration)
                )
            }

            // Action Buttons
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Shuffle Toggle",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3F),
                    )
                }
                FilledTonalButton(
                    onClick = onSeekToPreviousClick
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous Button"
                    )
                }

                var atEnd by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        onPlayPause()
                        atEnd = !atEnd
                    }
                ) {
                    Image(
                        imageVector = if (state.isPlaying){
                            Icons.Default.Pause
                        } else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause Button",
                        contentScale = ContentScale.Fit
                    )
                }
                FilledTonalButton(
                    onClick = onSeekToNextClick
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next Button"
                    )
                }

                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3F),
                        contentDescription = "Repeat Toggle"
                    )
                }
            }
        }

    }
}

fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}


@PreviewLightDark()
@Composable
private fun MusicPlayerScreenPreview() {
    MusicPlayerTheme {
        MusicPlayerScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            onPlayPause = {},
            onSeekToNextClick = {},
            onSeekToPreviousClick = {},
            state = SharedState(
                isPlaying = true,
                isLoading = false,
                selectedMusic = musicUiPreview,
                musicList = emptyList()
            ),
            mediaController = null
        )
    }
}

private var musicUiPreview = MusicUI(
    album = "Test Album",
    albumArt = null,
    artist = "Test Artist",
    duration = "300",
    id = 32L,
    path = Uri.EMPTY,
    title = "Test Title"
)