package com.hraj9258.musicplayer.music.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Size
import com.hraj9258.musicplayer.music.domain.Music
import com.hraj9258.musicplayer.music.domain.MusicDataSource

class LocalMusicDataSource(
    private val context: Context,
) : MusicDataSource {
    private val localMusicSortPreference = "SortOrder"

    override suspend fun getMusicList(): List<Music> {
        // ToDo: Implement Sorting
        val preference = context
            .getSharedPreferences(localMusicSortPreference,Context.MODE_PRIVATE)
        val sortOrderPreferences = preference
            .getString(localMusicSortPreference, "sortByName")
        val sortOrder = MediaStore.MediaColumns.DISPLAY_NAME + " ASC"

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, sortOrder)

        val musicList = mutableListOf<Music>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val album =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                val artist =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val duration =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val path =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))

                val perItemUri = ContentUris.withAppendedId(uri, id)

                var thumbnail: Bitmap? = null

                try {
                    thumbnail = context.contentResolver.loadThumbnail(
                        perItemUri,
                        Size(300, 300),
                        null
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val music = Music(
                    album = album,
                    albumArt = thumbnail,
                    artist = artist,
                    duration = duration,
                    id = id,
                    path = path,
                    title = title
                )
                musicList.add(music)
            }
        }
        cursor?.close()

        return musicList
    }


}