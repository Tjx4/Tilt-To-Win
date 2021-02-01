package com.hearxgroup.tilttowin.enum

import com.hearxgroup.tilttowin.R

enum class ArrowColors(var colorName: String, var colorRes: Int, var textColor: Int) {
    RedArrow("Red", R.color.redArrow, R.color.lightText),
    BlueArrow("Blue", R.color.blueArrow, R.color.lightText),
    YellowArrow("Yellow", R.color.yellowArrow, R.color.darkText),
    GreenArrow("Green", R.color.greenArrow, R.color.darkText)
}