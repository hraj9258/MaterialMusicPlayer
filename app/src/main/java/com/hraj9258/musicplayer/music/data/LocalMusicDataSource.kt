package com.hraj9258.musicplayer.music.data

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.hraj9258.musicplayer.music.domain.Music
import com.hraj9258.musicplayer.music.domain.MusicDataSource

class LocalMusicDataSource(
    val context: Context,
) : MusicDataSource {
    internal val localMusicSortPreference = "SortOrder"

    override suspend fun getMusicList(): List<Music> {
        // ToDo: Implement Sorting
        val preference = context
            .getSharedPreferences(localMusicSortPreference,Context.MODE_PRIVATE)
        val sortOrderPreferences = preference
            .getString(localMusicSortPreference, "sortByName")
        val sortOrder = MediaStore.MediaColumns.DISPLAY_NAME + " ASC"


        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder
        )

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
                val music = Music(
                    album = album,
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