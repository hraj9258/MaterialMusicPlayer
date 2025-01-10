package com.hraj9258.musicplayer.music.presentation

import android.app.Application
import android.content.ComponentName
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.hraj9258.musicplayer.music.data.LocalMusicDataSource
import com.hraj9258.musicplayer.music.presentation.models.MusicUI
import com.hraj9258.musicplayer.music.presentation.models.toMediaItem
import com.hraj9258.musicplayer.music.presentation.models.toMusicUI
import com.hraj9258.musicplayer.service.PlaybackService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SharedViewModel(
    application: Application,
    private val musicDataSource: LocalMusicDataSource
) : ViewModel() {
    var mediaController: MediaController? = null

    private val _state = MutableStateFlow(SharedState())
    val state = _state
        .onStart {
            loadMusic()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    val sessionToken = SessionToken(
        application.applicationContext,
        ComponentName(application.applicationContext, PlaybackService::class.java)
    )
    val controllerFuture = MediaController.Builder(
        application,
        sessionToken
    ).buildAsync()

    fun onSelectedMusic(musicUI: MusicUI?) {
        if (mediaController?.isPlaying == true) {
            mediaController?.pause()
        }
        val selectedMusic = _state.value.musicList.indexOf(musicUI)
        mediaController?.seekToDefaultPosition(selectedMusic)
        mediaController?.prepare()
        mediaController?.play()
        _state.update {
            it.copy(
                selectedMusic = musicUI,
                isPlaying = true
            )
        }

    }

    fun musicPlayPause() {
        if (state.value.isPlaying) {
            mediaController?.pause()
            _state.update { it.copy(isPlaying = false) }
        } else {
            mediaController?.play()
            _state.update { it.copy(isPlaying = true) }
        }
    }

    fun seekToNextMediaItem() {
        mediaController?.seekToNextMediaItem()
        updateSelectedMusic(mediaController?.currentMediaItemIndex!!)
    }

    fun seekToPreviousMediaItem() {
        mediaController?.seekToPreviousMediaItem()
        updateSelectedMusic(mediaController?.currentMediaItemIndex!!)
    }

    fun updateSelectedMusic(index: Int){
        val music = _state.value.musicList[index]
        _state.update { it.copy(
            selectedMusic = music
        ) }
    }

    fun getController() = mediaController

    fun loadMusic() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            val musicList = musicDataSource.getMusicList().map {
                it.toMusicUI()
            }
            _state.update {
                it.copy(
                    musicList = musicList,
                    isLoading = false
                )
            }
            initController()
        }
    }

    fun initController() {
        controllerFuture.addListener({
            mediaController = controllerFuture.get()

            mediaController?.addListener(PlayerEvents())

            _state.value.musicList.forEach { musicUi ->
                Handler(Looper.getMainLooper()).post {
                    mediaController?.addMediaItem(musicUi.toMediaItem())
                }
            }
        }, MoreExecutors.directExecutor())
    }
}

class PlayerEvents: Player.Listener{
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (isPlaying){

        }else{

        }
        Log.d("PlayerEvents", "onIsPlayingChanged: $isPlaying")
        super.onIsPlayingChanged(isPlaying)
    }

    override fun onVolumeChanged(volume: Float) {
        super.onVolumeChanged(volume)
        Log.d("PlayerEvents", "onVolumeChanged:")
    }
}