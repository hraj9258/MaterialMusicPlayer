package com.hraj9258.musicplayer.service

import android.content.Intent
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.ListenableFuture

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    // Create your Player and MediaSession in the onCreate lifecycle event
    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        player.repeatMode = Player.REPEAT_MODE_ALL
        mediaSession = MediaSession.Builder(this, player)
            .setCallback(MyMediaSessionCallback())
            .build()
    }

    // This example always accepts the connection request
    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? = mediaSession

    // The user dismissed the app from the recent tasks
    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player!!
        if (!player.playWhenReady
            || player.mediaItemCount == 0
            || player.playbackState == Player.STATE_ENDED) {
            // Stop the service if not playing, continue playing in the background
            // otherwise.
            stopSelf()
        }

    }

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}

@UnstableApi
class MyMediaSessionCallback : MediaSession.Callback{
    override fun onPlaybackResumption(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo
    ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
        Log.d("MyMediaSessionCallback: ", "onPlaybackResumption called")
//        mediaSession.
        mediaSession.player.play()
        return super.onPlaybackResumption(mediaSession, controller)
    }
}