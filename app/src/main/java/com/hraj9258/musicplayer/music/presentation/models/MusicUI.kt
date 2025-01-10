package com.hraj9258.musicplayer.music.presentation.models

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import com.hraj9258.musicplayer.music.domain.Music

data class MusicUI(
    val album: String,
    val albumArt: ImageBitmap? = null,
    val artist: String,
    val duration: String,
    val id: Long,
    val path: Uri,
    val title: String
)

fun Music.toMusicUI(): MusicUI{
    return MusicUI(
        album = album,
        albumArt = getAlbumArt(path),
        artist = artist,
        duration = duration.toString(),
        id = id,
        path = Uri.parse(path),
        title = title
    )
}

fun MusicUI.toMediaItem(): MediaItem{
    return MediaItem.fromUri(path)
}

@OptIn(UnstableApi::class)
fun MediaItem.toMusicUi(): MusicUI{
    return MusicUI(
        album = mediaMetadata.albumTitle.toString(),
        albumArt = getAlbumArt(mediaMetadata.artworkUri.toString()),
        artist = mediaMetadata.artist.toString(),
        duration = mediaMetadata.durationMs.toString(),
        id = mediaId.toLong(),
        path = mediaMetadata.artworkUri?: Uri.EMPTY,
        title = mediaMetadata.title.toString()
    )
}

fun getAlbumArt(path: String?): ImageBitmap? {
    if (path == null) return null
    val mediaMDR = MediaMetadataRetriever()
    mediaMDR.setDataSource(path)
    val embeddedPicture = mediaMDR.embeddedPicture
    if (embeddedPicture != null){
        val bm = BitmapFactory.decodeByteArray(embeddedPicture,0, embeddedPicture.size)
        return bm.asImageBitmap()
    } else{
        return null
    }
}


