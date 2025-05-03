package com.location.livetracker.base

import android.app.Application
import android.content.Context
import com.location.livetracker.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        startKoin {
            androidContext(this@AppClass)
            modules(appModule)
        }
    }

    companion object {
        var appContext: Context? = null
    }
}