package com.fetch.weather

import android.app.Application
import com.data.di.module.networkModule
import com.data.di.module.repositoryModule
import com.data.di.module.useCaseModule
import com.fetch.weather.di.module.appModule
import com.fetch.weather.di.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import timber.log.Timber.*
import timber.log.Timber.Forest.plant


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // Start Koin
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@App)
            androidFileProperties()
            modules(appModule)
            modules(networkModule)
            modules(repositoryModule)
            modules(useCaseModule)
            modules(viewModelModule)
        }

        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
    }
}
