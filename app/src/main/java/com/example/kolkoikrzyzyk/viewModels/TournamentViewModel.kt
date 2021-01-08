package com.example.kolkoikrzyzyk.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kolkoikrzyzyk.model.User

class TournamentViewModel(application: Application) : AndroidViewModel(application) {
    val players = mutableListOf<User>()

    fun addPlayer(user: User){
        players.add(user)
    }
    fun removePlayer(user: User){
        players.remove(user)
    }

    fun createTournament() {

    }

}