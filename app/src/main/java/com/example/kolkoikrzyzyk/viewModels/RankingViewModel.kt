package com.example.kolkoikrzyzyk.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kolkoikrzyzyk.UserRepository
import com.example.kolkoikrzyzyk.database.AppDatabase

class RankingViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "RankingViewModel"
    private val repository = UserRepository(AppDatabase.getInstance(application).userDao())

    val users = repository.getAllUsers()
}
