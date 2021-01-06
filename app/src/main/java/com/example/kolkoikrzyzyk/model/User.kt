package com.example.kolkoikrzyzyk.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid: Long, val name: String) : Parcelable