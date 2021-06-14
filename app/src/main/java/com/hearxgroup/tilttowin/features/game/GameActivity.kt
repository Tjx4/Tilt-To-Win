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
import com.hearxgroup.tilttowin.enum.TiltDirection
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
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private var lastDirection: Int? = null
    private var accelerationCurrentValue: Double = 0.0
    private var accelerationPrevValue: Double = 0.0

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
        sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    private fun addObservers() {
        gameViewModel.isInitCountDownFinished.observe(this, Observer {onInitCountDownFinished(it)})
        gameViewModel.colorIndex.observe(this, Observer {onColorSet(it)})
        gameViewModel.tiltDirection.observe(this, Observer {onRequiredDirectionSet(it)})
        gameViewModel.wrongChoice.observe(this, Observer {onWrongDirectionTilted(it)})
        gameViewModel.isWinGame.observe(this, Observer {onWinGame(it)})
        gameViewModel.isLoseGame.observe(this, Observer {onLoseGame(it)})
        gameViewModel.isRoundEnd.observe(this, Observer {onRoundEnd(it)})
        gameViewModel.isWinRound.observe(this, Observer {onWinRound(it)})
        gameViewModel.isLoseRound.observe(this, Observer {onLoseRound(it)})
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
        gameViewModel.startInitCountDown(3)
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

    private fun onWrongDirectionTilted(isWrongDirection: Boolean) {
        imgDirection.blinkView(0.6f, 0.3f, 150, 2, Animation.ABSOLUTE, 0, {
            tvTryAgain.visibility = View.VISIBLE
        })
    }

    private fun onRoundEnd(isEnd: Boolean) {
        imgDirection.visibility = View.GONE
        tvTryAgain.visibility = View.GONE
    }

    private fun onWinRound(isWin: Boolean){
        gameViewModel.initRound()
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

    private fun initSensor() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager?

        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST) //1000000)
        sensorManager?.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val x: Float = event?.values?.get(0) ?: 0.0f
        val y: Float = event?.values?.get(1) ?: 0.0f
        val z: Float = event?.values?.get(2) ?: 0.0f

        val zAngle = (z * 10).toInt()
        val yAngle = (y * 10).toInt()
        val requiredAngle = 50
        val threshold = (requiredAngle * 90 / 100)
        var direction = 4

        accelerationCurrentValue = Math.sqrt( (x * x + y * y + z * z).toDouble() )
        val changeInAcceleration = Math.abs(accelerationCurrentValue - accelerationPrevValue).toInt()
        accelerationPrevValue = accelerationCurrentValue

        if (zAngle > threshold || zAngle < -threshold) {
            val forward = zAngle < -requiredAngle && changeInAcceleration < 6
            val back = zAngle > requiredAngle && changeInAcceleration < 6

            when {
                forward -> {
                    direction = 2
                }
                back -> {
                    direction = 3
                }
            }

        } else {
            val left = yAngle < -requiredAngle && changeInAcceleration < 4
            val right = yAngle > requiredAngle && changeInAcceleration < 4

            when{
                left -> {
                    direction = 0
                }
                right -> {
                    direction = 1
                }
            }

        }

        val isZ = z > 5 || z < -5
        val isY = y > 5 || y < -5
        val isAllowed = isZ || isY

        if(isAllowed) {
            if(lastDirection != null && direction != lastDirection && direction < 4) {
                gameViewModel.setUserTiltDirection(direction)
                tvTryAgain.visibility = View.GONE
            }
        }

        lastDirection = direction
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    private fun onWinGame(isWin: Boolean){
        imgDirection.visibility = View.GONE
        tvTryAgain.visibility = View.GONE

        val score = "${gameViewModel.score.value} out of ${gameViewModel.round.value}"
        showSuccessAlert(this,
            getString(R.string.win_title),
            getString(R.string.game_win_message, score),
            getString(R.string.play_again),
            getString(R.string.close_app),
            {
                gameViewModel.resetGame()
            },
            {
                finish()
            }
        )
    }

    private fun onLoseGame(isLose: Boolean) {
        imgDirection.visibility = View.GONE
        tvTryAgain.visibility = View.GONE

        val score = "${gameViewModel.score.value} out of ${gameViewModel.round.value}"
        showErrorAlert(
            this,
            getString(R.string.lose_title),
            getString(R.string.game_lose_message, score),
            getString(R.string.play_again),
            getString(R.string.close_app),
            {
                gameViewModel.resetGame()
            },
            {
                finish()
            }
        )
    }

}