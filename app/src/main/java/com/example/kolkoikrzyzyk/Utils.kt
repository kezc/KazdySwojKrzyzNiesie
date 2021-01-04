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

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}