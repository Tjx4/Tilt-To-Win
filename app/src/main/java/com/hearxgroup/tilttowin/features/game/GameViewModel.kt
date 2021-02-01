package com.hearxgroup.tilttowin.features.game

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hearxgroup.tilttowin.R
import com.hearxgroup.tilttowin.base.viewModel.BaseVieModel
import com.hearxgroup.tilttowin.enum.TiltDirection
import com.hearxgroup.tilttowin.helpers.countDownTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : BaseVieModel(application) {

    private val _arrow: MutableLiveData<Int> = MutableLiveData()
    var arrow: MutableLiveData<Int> = MutableLiveData()
        get() = _arrow

    private val _countDown: MutableLiveData<Int> = MutableLiveData<Int>().apply { setValue(0) }
    val countDown: LiveData<Int>
        get() = _countDown

    private val _score: MutableLiveData<Int> = MutableLiveData<Int>().apply { setValue(0) }
    val score: LiveData<Int>
        get() = _score

    private val _attempts: MutableLiveData<Int> = MutableLiveData<Int>().apply { setValue(0) }
    val attempts: LiveData<Int>
        get() = _attempts

    private val _isCountDownFinished: MutableLiveData<Boolean> = MutableLiveData()
    val isCountDownFinished: LiveData<Boolean>
        get() = _isCountDownFinished

    private val _roundEndIcon: MutableLiveData<Int> = MutableLiveData()
    val roundEndIcon: LiveData<Int>
        get() = _roundEndIcon

    private val _roundEndMessage: MutableLiveData<String> = MutableLiveData()
    val roundEndMessage: LiveData<String>
        get() = _roundEndMessage

    private val _colorIndex: MutableLiveData<Int> = MutableLiveData()
    val colorIndex: LiveData<Int>
        get() = _colorIndex

    private var interval: Long = 2
    private var isInplay = false

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
            initRound()
        })
    }

    fun setColorAndInitGame(colorIndex: Int){
        _colorIndex.value = colorIndex
        initRound()
    }

    fun initRound(){
        isInplay = true
        setCurrentRoundTiltDirectionAndInterval()
    }

    fun setCurrentRoundTiltDirectionAndInterval(){
        val direction = (0..3).random()
        var interval = ((2..5).random() * 1000).toLong()

        ioScope.launch {
            delay(interval)

            uiScope.launch {
                _arrow.value = TiltDirection.values()[direction].directionIcon
            }
        }
    }

    fun setWinRound(){
        isInplay = false
        _score.value = _score.value?.plus(1)
        //_isWinRound.value = true

        _roundEndIcon.value = R.drawable.ic_victory
        _roundEndMessage.value = "Congratulations you won this round"
    }

    fun setLoseRound(){
        isInplay = false
        _score.value = _score.value?.minus(1)
       // _isLoseRound.value = true

        _roundEndIcon.value = R.drawable.ic_loss
        _roundEndMessage.value = "Sorry you have lost this round"
    }


    fun setUserTiltDirection(tiltDirection: Int){
        if(!isInplay) {
            return
        }

      //  _userTiltDirection.value = tiltDirection
    }

}