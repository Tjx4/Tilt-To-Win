package com.hearxgroup.tilttowin.base.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.hearxgroup.tilttowin.R
import com.hearxgroup.tilttowin.constants.LAYOUT

abstract class BaseDialogFragment : DialogFragment() {
    protected var clickedView: View? = null
    protected var activity: AppCompatActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogTheme

        val layout = arguments?.getInt(LAYOUT) ?: 0

        return if(layout == 0){
            super.onCreateView(inflater, container, savedInstanceState)!!
        }
        else{
            inflater.inflate(layout, container, false)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    protected fun setViewClickEvents(views: Array<View>) {
        for (view in views) {
            if (view == null)
                continue

            view.setOnClickListener { v -> onFragmentViewClickedEvent(v) }
        }
    }

    open fun hideLoaderAndShowEnterMessage() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity?
    }

    protected fun setListviewClickEvents(listView: ListView) {
        listView.setOnItemClickListener { parent, view, position, id ->
            onFragmentViewClickedEvent(view)
        }
    }

    protected fun onFragmentViewClickedEvent(view: View) {
        dialog?.dismiss()
    }

}