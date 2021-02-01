package com.hearxgroup.tilttowin.features.game.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hearxgroup.tilttowin.R
import com.hearxgroup.tilttowin.adapters.ArrowColorAdapter
import com.hearxgroup.tilttowin.base.fragments.BaseDialogFragment
import com.hearxgroup.tilttowin.enum.ArrowColors
import com.hearxgroup.tilttowin.extensions.blinkView
import com.hearxgroup.tilttowin.features.game.GameActivity

class ColorSelectorFragment : BaseDialogFragment(), ArrowColorAdapter.ColorClickListener {
    private lateinit var gameActivity: GameActivity
    private lateinit var colorsRv: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val parentView = super.onCreateView(inflater, container, savedInstanceState)
        initViews(parentView)
        return parentView
    }

    private fun initViews(parentView: View) {
        colorsRv = parentView.findViewById(R.id.rvColors)
        val searchTypeLayoutManager = GridLayoutManager(gameActivity, 2)
        searchTypeLayoutManager.initialPrefetchItemCount = ArrowColors.values().size
        colorsRv?.layoutManager = searchTypeLayoutManager
        val arrowColorAdapter = ArrowColorAdapter(gameActivity, ArrowColors.values())
        arrowColorAdapter.setOnHeroClickListener(this)
        colorsRv.adapter = arrowColorAdapter
    }

    override fun onColorClicked(view: View, position: Int) {
        view.blinkView(0.6f, 0.2f, 130, 2, Animation.ABSOLUTE, 0, {
            gameActivity.gameViewModel.setArrowColor(position)
            dismiss()
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        gameActivity = context as GameActivity
    }

    companion object {
        fun newInstance(): ColorSelectorFragment {
            val colorSelectorFragment = ColorSelectorFragment()
            colorSelectorFragment.arguments = Bundle()
            return  colorSelectorFragment
        }
    }
}