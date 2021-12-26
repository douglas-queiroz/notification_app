package com.douglasqueiroz.notification.di

import com.douglasqueiroz.notification.util.IconUtil
import com.douglasqueiroz.notification.util.PermissionUtil
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object UtilModule {

    fun get() = module {
        single { PermissionUtil(androidApplication()) }
        single { IconUtil(androidApplication()) }
    }
}