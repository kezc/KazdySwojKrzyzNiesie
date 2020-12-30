package com.example.kolkoikrzyzyk

import android.content.res.Resources
import android.util.TypedValue

fun Float.dpToPixels(resources: Resources): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        resources.displayMetrics
    )
}