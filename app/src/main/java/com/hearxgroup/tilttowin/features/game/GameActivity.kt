package com.hearxgroup.tilttowin.features.game

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.hearxgroup.tilttowin.R
import com.hearxgroup.tilttowin.base.activities.BaseActivity
import com.hearxgroup.tilttowin.databinding.ActivityGameBinding
import com.hearxgroup.tilttowin.enum.ArrowColors
import com.hearxgroup.tilttowin.extensions.blinkView
import com.hearxgroup.tilttowin.features.game.fragments.ColorSelectorFragment
import com.hearxgroup.tilttowin.features.game.fragments.RoundFinishedFragment
import com.hearxgroup.tilttowin.helpers.showDialogFragment
import com.hearxgroup.tilttowin.helpers.showErrorAlert
import com.hearxgroup.tilttowin.helpers.showSuccessAlert
import kotlinx.android.synthetic.main.activity_game.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameActivity : BaseActivity(), SensorEventListener {
    private lateinit var binding: ActivityGameBinding
    val gameViewModel: GameViewModel by viewModel()
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    private var lastDirection: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)
        binding.gameViewModel = gameViewModel
        binding.lifecycleOwner = this
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        addObservers()
        initSensor()
        showColorSelector()
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun initSensor() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager?
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun addObservers() {
        gameViewModel.isInitCountDownFinished.observe(this, Observer {onInitCountDownFinished(it)})
        gameViewModel.colorIndex.observe(this, Observer {onColorSet(it)})
        gameViewModel.tiltDirection.observe(this, Observer {onRequiredDirectionSet(it)})
        gameViewModel.userTiltDirection.observe(this, Observer {onUserTiltDirectionSet(it)})
        gameViewModel.wrongChoice.observe(this, Observer { onWrongDirectionTilted(it) })
        gameViewModel.isTimeRunOut.observe(this, Observer {onTimeRunOut(it)})
        gameViewModel.isWinRound.observe(this, Observer {onWinRound(it)})
        gameViewModel.isLoseRound.observe(this, Observer {onLoseRound(it)})
        gameViewModel.isWinGame.observe(this, Observer {onWinGame(it)})
        gameViewModel.isLoseGame.observe(this, Observer {onLoseGame(it)})
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    private fun showColorSelector() {
        val colorSelectorFragment = ColorSelectorFragment.newInstance()
        colorSelectorFragment?.isCancelable = false
        showDialogFragment(
            getString(R.string.color_selector),
            R.layout.fragment_color_selector,
            colorSelectorFragment,
            this
        )
    }

    private fun onColorSet(cIndex: Int){
        tvCountDown.visibility = View.VISIBLE
        gameViewModel.startCountDown(3)
    }

    private fun onInitCountDownFinished(isFinished: Boolean){
        tvCountDown.visibility = View.GONE
        clTopBanner.visibility = View.VISIBLE
        Toast.makeText(this, getString(R.string.game_begun), Toast.LENGTH_SHORT).show()
        gameViewModel.initRound()
    }

    private fun onRequiredDirectionSet(dIndex: Int){
        imgDirection.visibility = View.VISIBLE
        imgDirection.setColorFilter(ContextCompat.getColor(this, ArrowColors.values()[gameViewModel.colorIndex.value!!].colorRes))
    }

    private fun onUserTiltDirectionSet(directionIndex: Int) {
        gameViewModel.checkTiltDirectionMatch(directionIndex)
    }

    private fun onWrongDirectionTilted(isWrongDirection: Boolean) {
        imgDirection.blinkView(0.6f, 0.3f, 150, 2, Animation.ABSOLUTE, 0, {
            tvTryAgain.visibility = View.VISIBLE
        })
    }

    private fun onWinRound(isWin: Boolean){
        hideArrowAndInitRound()

    }

    private fun onLoseRound(isLose: Boolean) {
        val roundFinishedFragment = RoundFinishedFragment.newInstance()
        showDialogFragment(
            getString(R.string.round_complete),
            null,
            roundFinishedFragment,
            this
        )
    }

    private fun onTimeRunOut(isTimeOut: Boolean) {
        imgDirection.visibility = View.GONE
        tvTryAgain.visibility = View.GONE
    }

    private fun hideArrowAndInitRound() {
        imgDirection.visibility = View.GONE
        tvTryAgain.visibility = View.GONE
        gameViewModel.initRound()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val x: Float = event?.values?.get(0) ?: 0.0f
        val y: Float = event?.values?.get(1) ?: 0.0f
        val z: Float = event?.values?.get(2) ?: 0.0f

        val zAngle =  (z * 10).toInt()
        val yAngle = (y * 10).toInt()
        val requiredAngle = 50
        val threshold = (requiredAngle * 90 / 100)
        var direction = 4

        if (zAngle > threshold || zAngle < -threshold) {

            val forward = zAngle < -requiredAngle
            val back = zAngle > requiredAngle

            when {
                forward -> {
                    direction = 2
                }
                back -> {
                    direction = 3
                }
            }

        } else {
            val left = yAngle < -requiredAngle
            val right = yAngle > requiredAngle

            when{
                left -> {
                    direction = 0
                }
                right -> {
                    direction = 1
                }
            }
        }

        if(lastDirection != null && direction != lastDirection && direction < 4) {
            gameViewModel.setUserTiltDirection(direction)
            tvTryAgain.visibility = View.GONE
        }

        lastDirection = direction
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    private fun onWinGame(isWin: Boolean){
        imgDirection.visibility = View.GONE
        tvTryAgain.visibility = View.GONE

        sensorManager?.unregisterListener(this)

        val score = "${gameViewModel.score.value}/${gameViewModel.attempts.value}"
        showSuccessAlert(this,
            getString(R.string.win_title),
            getString(R.string.game_win_message, score),
            getString(R.string.close_app)) {
            finish()
        }
    }

    private fun onLoseGame(isLose: Boolean) {
        imgDirection.visibility = View.GONE
        tvTryAgain.visibility = View.GONE

        sensorManager?.unregisterListener(this)

        val score = "${gameViewModel.score.value}/${gameViewModel.attempts.value}"
        showErrorAlert(
            this,
            getString(R.string.lose_title),
            getString(R.string.game_lose_message, score),
            getString(R.string.close_app)
        ) {
            finish()
        }
    }

}