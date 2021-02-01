package com.hearxgroup.tilttowin.base.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hearxgroup.tilttowin.base.fragments.BaseDialogFragment
import com.hearxgroup.tilttowin.constants.ACTIVITY_TRANSITION
import com.hearxgroup.tilttowin.constants.PAYLOAD_KEY

abstract class BaseActivity : AppCompatActivity() {
    var activeDialogFragment: BaseDialogFragment? = null
    var isNewActivity: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTransitions(this)
        isNewActivity = true
        supportActionBar?.elevation = 0f
    }

    private fun initTransitions(activity: Activity) {
        try {
            val activityTransition = activity.intent.getBundleExtra(PAYLOAD_KEY)?.getIntArray(ACTIVITY_TRANSITION)
            activity.overridePendingTransition(activityTransition!![0], activityTransition[1])
        }
        catch (e: Exception) {
            Log.e("AT", "$e")
        }
    }
}