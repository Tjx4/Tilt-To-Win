package com.hearxgroup.tilttowin.extensions

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

fun View.blinkView(fromAlpha: Float, toAlpha: Float, duration: Long, startOffset: Long, repeatMode: Int, repeatCount: Int, onAnimationFinished: () -> Unit = {}, onAnimationStarted: () -> Unit = {}, onAnimationRepeated: () -> Unit = {}){
    val anim = getblinkViewAnination(fromAlpha, toAlpha, duration, startOffset, repeatMode, repeatCount, onAnimationFinished, onAnimationStarted, onAnimationRepeated)
    this.startAnimation(anim)
}

fun View.getblinkViewAnination(fromAlpha: Float, toAlpha: Float, duration: Long, startOffset: Long, repeatMode: Int, repeatCount: Int, onAnimationFinished: () -> Unit = {}, onAnimationStarted: () -> Unit = {}, onAnimationRepeated: () -> Unit = {}): AlphaAnimation {
    val anim = AlphaAnimation(fromAlpha, toAlpha)
    anim.duration = duration
    anim.startOffset = startOffset
    anim.repeatMode = repeatMode
    anim.repeatCount = repeatCount

    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            onAnimationStarted()
        }

        override fun onAnimationEnd(animation: Animation) {
            onAnimationFinished()
        }

        override fun onAnimationRepeat(animation: Animation) {
            onAnimationRepeated()
        }
    })

    return anim
}
