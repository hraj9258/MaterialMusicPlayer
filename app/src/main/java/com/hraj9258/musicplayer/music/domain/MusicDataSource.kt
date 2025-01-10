package com.hraj9258.musicplayer.music.domain

interface MusicDataSource {
    suspend fun getMusicList(): List<Music>

}