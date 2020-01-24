package com.nicktz.revoluttest

import android.app.Application
import com.nicktz.revoluttest.di.networkServiceModule
import org.koin.core.context.startKoin

class RevolutApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                networkServiceModule
            )
        }
    }
}