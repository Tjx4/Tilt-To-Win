package com.hearxgroup.tilttowin.features.game.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.hearxgroup.tilttowin.R
import com.hearxgroup.tilttowin.base.fragments.BaseDialogFragment
import com.hearxgroup.tilttowin.databinding.FragmentRoundFinishedBinding
import com.hearxgroup.tilttowin.features.game.GameActivity

class RoundFinishedFragment : BaseDialogFragment() {
    lateinit var binding: FragmentRoundFinishedBinding
    private lateinit var gameActivity: GameActivity
    private lateinit var iconImg: ImageView
    private lateinit var messageTv: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_round_finished,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.gameViewModel = gameActivity?.gameViewModel
        val parentView = binding.root

        initViews(parentView)
        gameActivity.gameViewModel.countDownToNextRound {
            dismiss()
        }
        return parentView
    }

    private fun initViews(parentView: View) {
        iconImg = parentView.findViewById(R.id.imgIcon)
        messageTv = parentView.findViewById(R.id.tvMessage)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        gameActivity = context as GameActivity
    }

    fun onBackPressed(): Boolean {
        return  false
    }

    companion object {
        fun newInstance(): RoundFinishedFragment {
            val roundFinishedFragment = RoundFinishedFragment()
            roundFinishedFragment.arguments = Bundle()
            return  roundFinishedFragment
        }
    }
}