package com.hearxgroup.tilttowin.enum

import com.hearxgroup.tilttowin.R

enum class TiltDirection(val directionName: String, val icon: Int) {
    Left("Left", R.drawable.ic_left),
    Right("Right", R.drawable.ic_right),
    Forward("Forward", R.drawable.ic_foward),
    Back("Back", R.drawable.ic_back)
}