package com.hearxgroup.tilttowin

import android.app.Application
import com.hearxgroup.tilttowin.di.ModuleLoadHelper
import com.hearxgroup.tilttowin.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(
                listOf(
                    viewModelModule
                ) + ModuleLoadHelper.getBuildSpecialModuleList()
            )
        }
    }
}