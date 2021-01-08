package com.example.kolkoikrzyzyk.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import com.example.kolkoikrzyzyk.repositories.UserRepository
import com.example.kolkoikrzyzyk.database.AppDatabase

class RankingViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "RankingViewModel"
    private val repository = UserRepository(AppDatabase.getInstance(application).userDao())

    val users =
        Transformations.map(repository.getAllUsers()) {
            it.sortedByDescending { user -> user.wonGames * 3 + user.drawnGames - 3 * user.lostGames }
        }
}
