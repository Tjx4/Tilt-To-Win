package com.hearxgroup.tilttowin.features.extensions

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hearxgroup.tilttowin.R
import com.hearxgroup.tilttowin.constants.ACTIVITY_TRANSITION
import com.hearxgroup.tilttowin.constants.PAYLOAD_KEY
import com.hearxgroup.tilttowin.features.models.Transition

val FADE_IN_ACTIVITY = getTransitionAnimation(R.anim.fade_in, R.anim.no_transition)
val FADE_OUT_ACTIVITY = getTransitionAnimation(R.anim.no_transition, R.anim.fade_out)

fun AppCompatActivity.navigateToActivity(activity: Class<*>, payload: Bundle?, transitionAnimation: Transition) {
    goToActivity(activity, transitionAnimation, payload)
}

private fun AppCompatActivity.goToActivity(activity: Class<*>, transitionAnimation: Transition, payload: Bundle?) {
    val intent = Intent(this, activity)

    val fullPayload = payload ?: Bundle()
    fullPayload.putIntArray(ACTIVITY_TRANSITION, intArrayOf(transitionAnimation.inAnimation, transitionAnimation.outAnimation))

    intent.putExtra(PAYLOAD_KEY, fullPayload)
    startActivity(intent)
}

private fun getTransitionAnimation(inAnimation: Int, outAnimation: Int): Transition {
    val transitionProvider = Transition()
    transitionProvider.inAnimation = inAnimation
    transitionProvider.outAnimation = outAnimation
    return transitionProvider
}