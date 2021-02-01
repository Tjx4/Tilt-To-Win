package co.za.platinumaccess.platinumaccess.features.selector.enum

import co.za.platinumaccess.platinumaccess.R

enum class TiltDirection(val directionName: String, val directionIcon: Int) {
    Left("Left", R.drawable.ic_left),
    Right("Right", R.drawable.ic_right),
    Forward("Forward", R.drawable.ic_foward),
    Back("Back", R.drawable.ic_back)
}