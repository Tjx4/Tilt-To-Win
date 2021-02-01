package com.hearxgroup.tilttowin.helpers

import com.hearxgroup.tilttowin.base.activities.BaseActivity
import com.hearxgroup.tilttowin.base.fragments.BaseDialogFragment
import com.hearxgroup.tilttowin.constants.LAYOUT
import com.hearxgroup.tilttowin.constants.TITLE

fun showDialogFragment(title: String, Layout: Int?, newFragmentBaseBase: BaseDialogFragment, activity: BaseActivity) {
    activity?.activeDialogFragment?.dismiss()
    val ft = activity.supportFragmentManager.beginTransaction()
    var newFragment = getFragmentDialog(title, Layout, newFragmentBaseBase)
    newFragment.show(ft, "dialog")
    activity.activeDialogFragment = newFragment
}

private fun getFragmentDialog(title: String, Layout: Int?, newFragmentBaseBase: BaseDialogFragment) : BaseDialogFragment {
    val payload = newFragmentBaseBase.arguments
    payload?.putString(TITLE, title)
    Layout?.let { payload?.putInt(LAYOUT, it) }

    newFragmentBaseBase.arguments = payload
    return newFragmentBaseBase
}
