package com.hearxgroup.tilttowin.features.game

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hearxgroup.tilttowin.R
import com.hearxgroup.tilttowin.base.viewModel.BaseVieModel
import com.hearxgroup.tilttowin.enum.TiltDirection
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : BaseVieModel(application) {
    private val _arrow: MutableLiveData<Int> = MutableLiveData()
    val arrow: LiveData<Int>
        get() = _arrow

    private val _countDown: MutableLiveData<Int> = MutableLiveData<Int>().apply { setValue(0) }
    val countDown: LiveData<Int>
        get() = _countDown

    private val _score: MutableLiveData<Int> = MutableLiveData<Int>().apply { setValue(0) }
    val score: LiveData<Int>
        get() = _score

    private val _attempt: MutableLiveData<Int> = MutableLiveData<Int>().apply { setValue(0) }
    val attempts: LiveData<Int>
        get() = _attempt

    private val _isCountDownFinished: MutableLiveData<Boolean> = MutableLiveData()
    val isCountDownFinished: LiveData<Boolean>
        get() = _isCountDownFinished

    private val _userTiltDirection: MutableLiveData<Int> = MutableLiveData()
    val userTiltDirection: LiveData<Int>
        get() = _userTiltDirection

    private val _wrongChoice: MutableLiveData<Boolean> = MutableLiveData()
    val wrongChoice: LiveData<Boolean>
        get() = _wrongChoice

    private val _isWinRound: MutableLiveData<Boolean> = MutableLiveData()
    val isWinRound: MutableLiveData<Boolean>
        get() = _isWinRound

    private val _isTimeRunOut: MutableLiveData<Boolean> = MutableLiveData()
    val isTimeRunOut: MutableLiveData<Boolean>
        get() = _isTimeRunOut

    private val _isLoseRound: MutableLiveData<Boolean> = MutableLiveData()
    val isLoseRound: MutableLiveData<Boolean>
        get() = _isLoseRound

    private val _isWinGame: MutableLiveData<Boolean> = MutableLiveData()
    val isWinGame: LiveData<Boolean>
        get() = _isWinGame

    private val _isLoseGame: MutableLiveData<Boolean> = MutableLiveData()
    val isLoseGame: LiveData<Boolean>
        get() = _isLoseGame

    private val _roundEndIcon: MutableLiveData<Int> = MutableLiveData()
    val roundEndIcon: LiveData<Int>
        get() = _roundEndIcon

    private val _roundEndMessage: MutableLiveData<String> = MutableLiveData()
    val roundEndMessage: LiveData<String>
        get() = _roundEndMessage

    private val _colorIndex: MutableLiveData<Int> = MutableLiveData()
    val colorIndex: LiveData<Int>
        get() = _colorIndex

    private val _tiltDirection: MutableLiveData<Int> = MutableLiveData()
    val tiltDirection: LiveData<Int>
        get() = _tiltDirection

    private var roundTimer: CountDownTimer? = null
    private var maxAttempts: Int = 10
    private var isInplay = false
    private var isLegal = false
    private var stopTimer  = false

    private fun countDown(from: Int, onTicCallback: (Int) -> Unit = {}, onCompleteCallback: () -> Unit = {}){
        if(from > 0){
            onTicCallback.invoke(from)
            ioScope.launch {
                delay(1000)
                uiScope.launch {
                    startCountDown(from - 1)
                }
            }
        }
        else{
            onCompleteCallback.invoke()
            //End coroutine
            //viewModelJob.complete()
            //uiScope.cancel()
            //ioScope.cancel()
        }
    }

    fun startCountDown(from: Int){
        countDown(from, { count ->
            _countDown.value = count
        }, {
            _isCountDownFinished.value = true
        })
    }

    fun countDownToNextRound(onCompleteCallback: () -> Unit = {}){
        viewModelJob = Job()
        countDown(5, {
            _countDown.value = it.toInt()
        } , {
            onCompleteCallback.invoke()
            initRound()
        })
    }

    fun startRoundCountDown(onCompleteCallback: () -> Unit = {}){
        roundTimer = countDownTime(3, {} , {
            if(!stopTimer){
                checkAndSetTooLateResponse()
            }
        })
        roundTimer?.start()
    }

    private fun checkAndSetTooLateResponse() {
        if (_attempt.value!! < maxAttempts) {
            _isTimeRunOut.value = true
            tooLateResponseLoss()
        } else {
            showGameWinOrLose()
        }
    }

    private fun showGameWinOrLose() {
        if (_score.value!! > 4) {
            _isWinGame.value = true
        } else {
            _isLoseGame.value = true
        }

        roundTimer?.cancel()
    }

    fun setArrowColor(colorIndex: Int){
        _colorIndex.value = colorIndex
    }

    fun initRound(){
        isInplay = true
        setRequiredDirectionAndInterval()
    }

    fun setRequiredDirectionAndInterval(){
        val direction = (0..3).random()
        var interval = ((2..5).random() * 1000).toLong()

        ioScope.launch {
            delay(interval)

            uiScope.launch {
                if(isInplay){
                    isLegal = true
                    _tiltDirection.value = direction
                    _arrow.value = TiltDirection.values()[direction].directionIcon
                    startRoundCountDown()
                }
            }
        }
    }

    fun setWinRound(){
        roundTimer?.cancel()
        isInplay = false
        isLegal = false
        _score.value = _score.value?.plus(1)
        _attempt.value = _attempt.value?.plus(1)
        _isWinRound.value = true
    }

    fun setLoseRound(){
        roundTimer?.cancel()
        isInplay = false
        isLegal = false
        _attempt.value = _attempt.value?.plus(1)
         _isLoseRound.value = true
        _roundEndIcon.value = R.drawable.ic_loss
    }

    fun tooEarlyResponseLoss(){
        setLoseRound()
        _score.value = _score.value?.minus(1)
        _roundEndMessage.value = app.getString(R.string.too_early_loss_message)
    }

    fun tooLateResponseLoss(){
        setLoseRound()
        _roundEndMessage.value = app.getString(R.string.too_late_loss_message)
    }

    fun setUserTiltDirection(tiltDirection: Int){
        if(!isInplay) {
            return
        }

        _userTiltDirection.value = tiltDirection
    }

    fun checkTiltDirectionMatch(directionIndex: Int) {
        if(_attempt.value!! < maxAttempts){
            if(!isLegal){
                tooEarlyResponseLoss()
            }
            else if(directionIndex == _tiltDirection.value){
                setWinRound()
            }
            else{
                _wrongChoice.value = true
            }
        }
        else {
            showGameWinOrLose()
        }

    }

}