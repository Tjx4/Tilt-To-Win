package com.hearxgroup.tilttowin.features.game

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hearxgroup.tilttowin.R
import com.hearxgroup.tilttowin.base.activities.BaseActivity
import com.hearxgroup.tilttowin.databinding.ActivityGameBinding
import com.hearxgroup.tilttowin.features.game.fragments.ColorSelectorFragment
import com.hearxgroup.tilttowin.helpers.showDialogFragment
import com.hearxgroup.tilttowin.helpers.showErrorAlert
import com.hearxgroup.tilttowin.helpers.showSuccessAlert
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : BaseActivity(), SensorEventListener {
    private lateinit var binding: ActivityGameBinding
    lateinit var gameViewModel: GameViewModel
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    var lastDirection: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var application = requireNotNull(this).application
        var viewModelFactory = GameViewModelFactory(application)

        gameViewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game)
        binding.gameViewModel = gameViewModel
        binding.lifecycleOwner = this

        addObservers()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun addObservers() {
        gameViewModel.isCountDownFinished.observe(this, Observer {onCountDownFinished(it)})
        gameViewModel.colorIndex.observe(this, Observer {onColorSet(it)})
        gameViewModel.isWinGame.observe(this, Observer {onWinGame(it)})
        gameViewModel.isLoseGame.observe(this, Observer {onLoseGame(it)})
    }

    private fun onCountDownFinished(isFinished: Boolean){
        tvCountDown.visibility = View.GONE
        showSelectColor()
    }

    private fun showSelectColor() {
        val colorSelectorFragment = ColorSelectorFragment.newInstance()
        colorSelectorFragment?.isCancelable = false
        showDialogFragment(
            "Color selector",
            R.layout.fragment_color_selector,
            colorSelectorFragment,
            this
        )
    }

    private fun onColorSet(cIndx: Int){
        clTopBanner.visibility = View.VISIBLE
        Toast.makeText(this, getString(R.string.game_begun), Toast.LENGTH_SHORT).show()
    }

    private fun onWinGame(isWin: Boolean){
        imgDirection.visibility = View.GONE
        showSuccessAlert(this, getString(R.string.win_title), getString(R.string.game_win_message), getString(R.string.close_app)) {
            finish()
        }
    }

    private fun onLoseGame(isLose: Boolean) {
        imgDirection.visibility = View.GONE
        showErrorAlert(
            this,
            getString(R.string.lose_title),
             getString(R.string.game_lose_message),
            getString(R.string.close_app)
        ) {
            finish()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val x: Float = event?.values?.get(0) ?: 0.0f
        val y: Float = event?.values?.get(1) ?: 0.0f
        val z: Float = event?.values?.get(2) ?: 0.0f

        val zAngle =  (z * 10).toInt()
        val yAngle = (y * 10).toInt()
        val requiredAngle = 50
        val threshold = (requiredAngle * 80 / 100)
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

}