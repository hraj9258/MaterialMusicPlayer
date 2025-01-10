package com.hraj9258.musicplayer.music.presentation.music_list

import com.hraj9258.musicplayer.music.presentation.models.MusicUI

sealed interface MusicListAction{
    data class OnMusicListClick(val musicUi: MusicUI): MusicListAction
}