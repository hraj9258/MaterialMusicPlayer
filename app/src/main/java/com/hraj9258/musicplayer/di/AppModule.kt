package com.hraj9258.musicplayer.di

import com.hraj9258.musicplayer.music.data.LocalMusicDataSource
import com.hraj9258.musicplayer.music.presentation.SharedViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single{
        LocalMusicDataSource(androidContext())
    }
    viewModelOf(::SharedViewModel)
}