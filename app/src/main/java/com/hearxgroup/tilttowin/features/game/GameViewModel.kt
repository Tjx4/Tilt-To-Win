package com.hearxgroup.tilttowin.features.game

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hearxgroup.tilttowin.R
import com.hearxgroup.tilttowin.base.viewModel.BaseVieModel
import com.hearxgroup.tilttowin.enum.TiltDirection
import kotlinx.coroutines.*

class GameViewModel(application: Application) : BaseVieModel(application) {
    private val _arrow: MutableLiveData<Int> = MutableLiveData()
    val arrow: LiveData<Int>
        get() = _arrow

    private val _countDownTime: MutableLiveData<Int> = MutableLiveData<Int>().apply { setValue(0) }
    val countDownTime: LiveData<Int>
        get() = _countDownTime

    private val _score: MutableLiveData<Int> = MutableLiveData<Int>().apply { setValue(0) }
    val score: LiveData<Int>
        get() = _score

    private val _currentRound: MutableLiveData<Int> = MutableLiveData<Int>().apply { setValue(1) }
    val round: LiveData<Int>
        get() = _currentRound

    private val _isInitCountDownFinished: MutableLiveData<Boolean> = MutableLiveData()
    val isInitCountDownFinished: LiveData<Boolean>
        get() = _isInitCountDownFinished

    private val _userTiltDirection: MutableLiveData<Int> = MutableLiveData()
    val userTiltDirection: LiveData<Int>
        get() = _userTiltDirection

    private val _wrongChoice: MutableLiveData<Boolean> = MutableLiveData()
    val wrongChoice: LiveData<Boolean>
        get() = _wrongChoice

    private val _isWinRound: MutableLiveData<Boolean> = MutableLiveData()
    val isWinRound: MutableLiveData<Boolean>
        get() = _isWinRound

    private val _isRoundEnd: MutableLiveData<Boolean> = MutableLiveData()
    val isRoundEnd: MutableLiveData<Boolean>
        get() = _isRoundEnd

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

    private var roundJob:Job? = null
    private var roundIoScope:CoroutineScope? = null
    private var roundUiScope:CoroutineScope? = null
    private var maxAttempts: Int = 10
    private var isInplay = false
    private var isLegal = false

    fun setArrowColor(colorIndex: Int){
        _colorIndex.value = colorIndex
    }

    fun countDownAndExecute(time: Int, onCompleteCallback: () -> Unit = {}){
        _countDownTime.value = time
        ioScope.launch {
            delay(1000)
            uiScope.launch {
                when (time) {
                    0 -> onCompleteCallback.invoke()
                    else -> countDownAndExecute(time - 1, onCompleteCallback)
                }
            }
        }
    }

    fun startInitCountDown(from: Int){
        countDownAndExecute(from) {
            _isInitCountDownFinished.value = true
        }
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
                    startRound(direction)
                }
            }
        }
    }

    private fun startRound(direction: Int) {
        isLegal = true
        setRequiredTiltDirection(direction)
        roundJob = Job()
        roundJob?.let {
            roundIoScope = CoroutineScope(Dispatchers.IO + it)
            roundUiScope = CoroutineScope(Dispatchers.Main + it)
        }
        startRoundCountDown(3)
    }

    private fun endRound() {
        roundJob?.cancel()
        roundIoScope?.cancel()
        roundUiScope?.cancel()
        isInplay = false
        isLegal = false
        _isRoundEnd.value = true
    }

    private fun setRequiredTiltDirection(direction: Int) {
        _tiltDirection.value = direction
        _arrow.value = TiltDirection.values()[direction].icon
    }

    fun startRoundCountDown(from: Int){
        roundIoScope?.launch {
            delay(1000)
            roundUiScope?.launch {
                when (from) {
                    0 ->  checkAndInitRound { tooLateResponseLoss() }
                    else -> startRoundCountDown(from - 1)
                }
            }
        }
    }

    fun setUserTiltDirection(tiltDirection: Int){
        _userTiltDirection.value = tiltDirection
        checkTiltDirectionMatch(tiltDirection)
    }

    fun checkTiltDirectionMatch(directionIndex: Int) {
        if(!isInplay) return

        when{
            !isLegal -> {
                _score.value = _score.value?.minus(1)
                checkAndInitRound { tooEarlyResponseLoss() }
            }
            directionIndex == _tiltDirection.value -> {
                _score.value = _score.value?.plus(1)
                checkAndInitRound { winRound() }
            }
            else -> _wrongChoice.value = true
        }
    }

    fun countDownToNextRound(onCompleteCallback: () -> Unit = {}){
        countDownAndExecute(3, onCompleteCallback)
    }

    private fun showGameWinOrLose(){
        endRound()
        if (_score.value!! > 4) {
            _isWinGame.value = true
        } else {
            _isLoseGame.value = true
        }
    }

    fun checkAndInitRound(onProceedCallback: () -> Unit){
        when {
            round.value == maxAttempts -> showGameWinOrLose()
            else -> onProceedCallback.invoke()
        }
    }

    fun winRound(){
        endRound()
        _currentRound.value = _currentRound.value?.plus(1)
        _isWinRound.value = true
        _roundEndIcon.value = R.drawable.ic_win
        _roundEndMessage.value = app.getString(R.string.win_message)
    }

    fun loseRound(){
        endRound()
        _currentRound.value = _currentRound.value?.plus(1)
         _isLoseRound.value = true
        _roundEndIcon.value = R.drawable.ic_loss
    }

    private fun tooEarlyResponseLoss(){
        loseRound()
        _roundEndMessage.value = app.getString(R.string.too_early_loss_message)
    }

    private fun tooLateResponseLoss(){
        loseRound()
        _roundEndMessage.value = app.getString(R.string.too_late_loss_message)
    }

    fun resetGame(){
        _score.value = 0
        _currentRound.value = 1
        initRound()
    }

}