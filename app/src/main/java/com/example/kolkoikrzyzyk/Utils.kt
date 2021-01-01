package com.example.kolkoikrzyzyk

import android.content.res.Resources
import android.util.TypedValue
import androidx.lifecycle.MutableLiveData

fun Float.dpToPixels(resources: Resources): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        resources.displayMetrics
    )
}


fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}