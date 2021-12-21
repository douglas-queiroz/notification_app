package com.douglasqueiroz.notification.di

import com.douglasqueiroz.notification.ui.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {

    fun get() = module {
        viewModel { MainViewModel(get()) }
    }
}
