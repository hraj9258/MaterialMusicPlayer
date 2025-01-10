package com.hraj9258.musicplayer

import android.app.Application
import com.hraj9258.musicplayer.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MusicPlayerApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@MusicPlayerApp)
            androidLogger()
            modules(appModule)
        }
    }
}