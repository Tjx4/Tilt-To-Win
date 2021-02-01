package com.hearxgroup.tilttowin.helpers

import android.os.CountDownTimer
import java.util.concurrent.TimeUnit


fun countDownTime(from: Long, onTickCallback: (Long) -> Unit, onCompleteCallback: () -> Unit){
    val seconds  = from * 1000

    object : CountDownTimer(seconds, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val sec = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

            onTickCallback.invoke(sec + 1)
        }

        override fun onFinish() {
            onCompleteCallback.invoke()
            this.cancel()
        }
    }.start()
}