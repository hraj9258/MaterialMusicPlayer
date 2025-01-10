package com.hraj9258.musicplayer.music.presentation

import com.hraj9258.musicplayer.music.presentation.models.MusicUI

data class SharedState(
    val isLoading : Boolean = true,
    val isPlaying : Boolean = false,
    val musicList : List<MusicUI> = emptyList(),
    val selectedMusic : MusicUI? = null
)
