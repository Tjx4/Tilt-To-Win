package com.hearxgroup.tilttowin.features.game

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
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : BaseActivity() {
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
        gameViewModel.isCountDownFinished.observe(this, Observer {onCountDownFinished(it)})
        gameViewModel.colorIndex.observe(this, Observer {onColorSet(it)})
        gameViewModel.initRound.observe(this, Observer {onInitRound(it)})
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

        //Init round
    }

    private fun onInitRound(initRound: Boolean){
        val ldfd = initRound
    }
}