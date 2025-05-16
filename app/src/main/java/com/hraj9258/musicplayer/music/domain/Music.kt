package com.hraj9258.musicplayer.music.domain

import android.graphics.Bitmap

data class Music(
    val album: String,
    val albumArt: Bitmap? = null,
    val artist: String,
    val duration: Long,
    val id: Long,
    val path: String,
    val title: String
)
