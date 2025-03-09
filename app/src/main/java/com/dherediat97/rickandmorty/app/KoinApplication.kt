package com.dherediat97.rickandmorty.app

import android.app.Application
import com.dherediat97.rickandmorty.di.repositoriesModule
import com.dherediat97.rickandmorty.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class KoinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(androidContext = this@KoinApplication)

            modules(
                repositoriesModule,
                viewModelsModule
            )
        }
    }
}