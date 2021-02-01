package com.hearxgroup.tilttowin.features.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hearxgroup.tilttowin.R
import com.hearxgroup.tilttowin.databinding.ActivityGameBinding
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    lateinit var gameViewModel: GameViewModel

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
        gameViewModel.isCountDownFininished.observe(this, Observer {onCountDownFinished(it)})
        //gameViewModel.colorIndex.observe(this, Observer {onCountDownFinished(it)})
    }

    private fun onCountDownFinished(isFinished: Boolean){
        tvCountDown.visibility = View.GONE
        onCountDownFinished()
    }

    private fun onCountDownFinished(){

    }

    private fun onCountDownFinished(cIndx: Int){
        Toast.makeText(this, "Game has Begun!", Toast.LENGTH_SHORT).show()
    }
}