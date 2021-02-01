package com.hearxgroup.tilttowin.features.game

import android.app.Application
import androidx.databinding.adapters.NumberPickerBindingAdapter.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hearxgroup.tilttowin.base.viewModel.BaseVieModel

class GameViewModel(application: Application) : BaseVieModel(application) {

    private val _arrow: MutableLiveData<Int> = MutableLiveData()
    var arrow: MutableLiveData<Int> = MutableLiveData()
        get() = _arrow

    private val _countDown: MutableLiveData<Int> = MutableLiveData<Int>().apply { setValue(0) }
    val countDown: LiveData<Int>
        get() = _countDown

    private val _currentPlayer: MutableLiveData<String> = MutableLiveData()
    val currentPlayer: LiveData<String>
        get() = _currentPlayer

    private val _score: MutableLiveData<Int> = MutableLiveData<Int>().apply { setValue(0) }
    val score: LiveData<Int>
        get() = _score
}