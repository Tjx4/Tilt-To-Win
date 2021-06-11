package com.hearxgroup.tilttowin.di

import com.hearxgroup.tilttowin.features.game.GameViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GameViewModel(androidApplication()) }
}
