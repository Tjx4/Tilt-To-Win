package com.hearxgroup.tilttowin.helpers

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.Window
import com.hearxgroup.tilttowin.R
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

fun showSuccessAlert(context: Context, title: String, message: String, buttonText: String = "Ok", callbackFun:  () -> Unit = {}){
    val ab = setupBasicMessage(title, message, buttonText, "", "", callbackFun, {}, {}, context)
    ab.setIcon(R.drawable.ic_victory)
    ab.setCancelable(false)
    showAlertMessage(ab, context)
}

fun showErrorAlert(context: Context, title: String, message: String, buttonText: String = "Ok", callbackFun: () -> Unit = {}){
    val ab = setupBasicMessage(title, message, buttonText, "", "", callbackFun, {}, {}, context)
    ab.setIcon(R.drawable.ic_loss)
    ab.setCancelable(false)
    showAlertMessage(ab, context)
}

private fun setupBasicMessage(title: String, message: String,
                              positiveButtonText: String?, neutralButtonText: String?, negativeButtonText: String?,
                              positiveCallback: () -> Unit, neutralCallback: () -> Unit, negativeCallback: () -> Unit,
                              context: Context
): AlertDialog.Builder {

    val ab = AlertDialog.Builder(context, R.style.AlertDialogCustom)
    ab.setMessage(message)
        .setTitle(title)
        .setPositiveButton(positiveButtonText) { dialogInterface, i ->
            positiveCallback()
        }

    if (neutralButtonText != null) {
        ab.setNeutralButton(neutralButtonText) { dialogInterface, i ->
            neutralCallback()
        }
    }

    if (negativeButtonText != null) {
        ab.setNegativeButton(negativeButtonText) { dialogInterface, i ->
            negativeCallback()
        }
    }

    return ab
}

private fun showAlertMessage(ab: AlertDialog.Builder, context: Context) {
    val a = ab.create()
    a.requestWindowFeature(Window.FEATURE_NO_TITLE)
    a.show()

    a.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.darkText))
    a.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.resources.getColor(R.color.darkText))
    a.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(context.resources.getColor(R.color.darkText))
}