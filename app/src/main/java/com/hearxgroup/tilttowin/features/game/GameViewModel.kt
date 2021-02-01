package com.hearxgroup.tilttowin.features.game

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hearxgroup.tilttowin.base.viewModel.BaseVieModel
import com.hearxgroup.tilttowin.helpers.countDownTime

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

    private val _isCountDownFinished: MutableLiveData<Boolean> = MutableLiveData()
    val isCountDownFinished: LiveData<Boolean>
        get() = _isCountDownFinished

    private val _roundEndIcon: MutableLiveData<Int> = MutableLiveData()
    val roundEndIcon: LiveData<Int>
        get() = _roundEndIcon

    private val _arrowColorIndex: MutableLiveData<Int> = MutableLiveData()
    val arrowColorIndex: LiveData<Int>
        get() = _arrowColorIndex

    private val _roundEndMessage: MutableLiveData<String> = MutableLiveData()
    val roundEndMessage: LiveData<String>
        get() = _roundEndMessage

    private val _colorIndex: MutableLiveData<Int> = MutableLiveData()
    val colorIndex: LiveData<Int>
        get() = _colorIndex

    private val _initRound: MutableLiveData<Boolean> = MutableLiveData()
    val initRound: LiveData<Boolean>
        get() = _initRound

    private var interval: Long = 2

    init {
        startCountDown()
    }

    fun startCountDown(){
        countDownTime(3, {
            _countDown.value = it.toInt()
        } , {
            _isCountDownFinished.value = true
        })
    }

    fun roundCountDown(onCompleteCallback: () -> Unit = {}){
        countDownTime(5, {
            _countDown.value = it.toInt()
        } , {
            _initRound.value = true
        })
    }

    fun setColorIndx(colorIndex: Int){
        _colorIndex.value = colorIndex
    }
}