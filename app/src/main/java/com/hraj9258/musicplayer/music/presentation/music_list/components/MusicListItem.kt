package com.hraj9258.musicplayer.music.presentation.music_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.hraj9258.musicplayer.music.presentation.models.MusicUI
import com.hraj9258.musicplayer.core.presentation.ui.theme.MusicPlayerTheme
import androidx.core.net.toUri

@Composable
fun MusicListItem(
    musicUi: MusicUI,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (musicUi.albumArt != null){
            Image(
                bitmap = musicUi.albumArt,
                contentDescription = musicUi.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(44.dp)
                    .width(56.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
        } else {
            Icon(
                imageVector = Icons.TwoTone.MusicNote,
                contentDescription = "Music Note",
                modifier = Modifier
                    .height(44.dp)
                    .width(56.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = musicUi.title,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                modifier = Modifier
                    .basicMarquee()
            )
            Text(
                text = musicUi.artist,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

    }
}

@PreviewFontScale
@PreviewDynamicColors
@PreviewLightDark
@Composable
private fun SongListItemPreview(
) {
    MusicPlayerTheme {
        MusicListItem(
            musicUi = previewMusicUI,
            onClick = { },
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.background
                )
        )
    }
}


internal val previewMusicUI = MusicUI(
    album = "Album Name",
    artist = "Artist Name",
    duration = "30000",
    id = 0,
    title = "Song Title",
    path = "".toUri()
)