package com.hearxgroup.tilttowin.features.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.hearxgroup.tilttowin.R
import com.hearxgroup.tilttowin.databinding.ActivityGameBinding
import java.util.Observer

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
//  pairViewModel.onPairedWithHost.observe(this, Observer {onHostDevicePaired(it)})
    }
}