package com.hearxgroup.tilttowin.features.game.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.hearxgroup.tilttowin.R
import com.hearxgroup.tilttowin.base.fragments.BaseDialogFragment
import com.hearxgroup.tilttowin.databinding.FragmentRoundFinishedBinding
import com.hearxgroup.tilttowin.features.game.GameActivity
import com.hearxgroup.tilttowin.features.game.GameViewModel

class RoundFinishedFragment : BaseDialogFragment() {
    private lateinit var binding: FragmentRoundFinishedBinding
    private lateinit var gameActivity: GameActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        gameActivity = context as GameActivity
    }

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameActivity?.gameViewModel.let {
            it.countDownToNextRound {
                dismiss()
                it.initRound()
            }
        }
    }

    companion object {
        fun newInstance(): RoundFinishedFragment {
            val roundFinishedFragment = RoundFinishedFragment()
            roundFinishedFragment.arguments = Bundle()
            return  roundFinishedFragment
        }
    }
}