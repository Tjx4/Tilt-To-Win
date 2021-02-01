package com.hearxgroup.tilttowin.base.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseVieModel(application: Application) : AndroidViewModel(application){
    protected var app = application
    protected var viewModelJob = Job()
    protected val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    protected val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}