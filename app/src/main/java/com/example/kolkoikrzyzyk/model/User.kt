package com.example.kolkoikrzyzyk.model

import android.os.Parcelable
import com.example.kolkoikrzyzyk.database.DbUser
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val uid: Long,
    val name: String,
    val password: String,
    var wonGames: Int = 0,
    var drawnGames: Int = 0,
    var lostGames: Int = 0
) : Parcelable {
    fun toDbUser(isLogged: Boolean) = DbUser(uid, name, password, isLogged, wonGames, drawnGames, lostGames)
}