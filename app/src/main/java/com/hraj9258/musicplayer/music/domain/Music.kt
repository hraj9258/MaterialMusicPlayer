package com.hraj9258.musicplayer.music.domain

data class Music(
    val album: String,
    val artist: String,
    val duration: Long,
    val id: Long,
    val path: String,
    val title: String
)
