package com.hearxgroup.tilttowin.features.game.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.recyclerview.widget.GridLayoutManager
import com.hearxgroup.tilttowin.adapters.ArrowColorAdapter
import com.hearxgroup.tilttowin.base.fragments.BaseDialogFragment
import com.hearxgroup.tilttowin.enum.ArrowColors
import com.hearxgroup.tilttowin.extensions.blinkView
import com.hearxgroup.tilttowin.features.game.GameActivity
import com.hearxgroup.tilttowin.features.game.GameViewModel
import kotlinx.android.synthetic.main.fragment_color_selector.*

class ColorSelectorFragment : BaseDialogFragment(), ArrowColorAdapter.ColorClickListener {
    private lateinit var gameActivity: GameActivity
    lateinit var gameViewModel: GameViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        gameActivity = context as GameActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        gameViewModel = gameActivity?.gameViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchTypeLayoutManager = GridLayoutManager(gameActivity, 2)
        searchTypeLayoutManager.initialPrefetchItemCount = ArrowColors.values().size
        rvColors?.layoutManager = searchTypeLayoutManager
        val arrowColorAdapter = ArrowColorAdapter(gameActivity, ArrowColors.values())
        arrowColorAdapter.setOnHeroClickListener(this)
        rvColors.adapter = arrowColorAdapter
    }

    override fun onColorClicked(view: View, position: Int) {
        view.blinkView(0.6f, 0.2f, 130, 2, Animation.ABSOLUTE, 0, {
            gameViewModel.setArrowColor(position)
            dismiss()
        })
    }

    companion object {
        fun newInstance(): ColorSelectorFragment {
            val colorSelectorFragment = ColorSelectorFragment()
            colorSelectorFragment.arguments = Bundle()
            return  colorSelectorFragment
        }
    }
}