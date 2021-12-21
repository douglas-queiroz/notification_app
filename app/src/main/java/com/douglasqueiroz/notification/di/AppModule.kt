package com.douglasqueiroz.notification.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object AppModule {

    fun startKoin(app: Application) = startKoin {
        androidContext(app)
        modules(
            listOf(
                ViewModelModule.get(),
                ServiceModule.get(),
                UtilModule.get(),
                RepositoryModule.get()
            )
        )
    }
}
