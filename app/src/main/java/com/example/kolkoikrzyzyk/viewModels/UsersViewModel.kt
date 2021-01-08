package com.example.kolkoikrzyzyk.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.kolkoikrzyzyk.Event
import com.example.kolkoikrzyzyk.repositories.UserRepository
import com.example.kolkoikrzyzyk.database.AppDatabase
import com.example.kolkoikrzyzyk.model.User
import kotlinx.coroutines.*

class UsersViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(AppDatabase.getInstance(application).userDao())

    val users = repository.getLoggedUsers()

    private val _operationSuccessful = MutableLiveData<Event<Boolean>>()
    val operationSuccessful: LiveData<Event<Boolean>>
        get() = _operationSuccessful

    private val _loginSuccessful = MutableLiveData<Event<Boolean>>()
    val loginSuccessful: LiveData<Event<Boolean>>
        get() = _loginSuccessful

    private val _noUsersLeft = MutableLiveData<Event<Boolean>>()
    val noUsersLeft: LiveData<Event<Boolean>>
        get() = _noUsersLeft

    var message = ""

    fun login(name: String, password: String) = viewModelScope.launch {
        val user = withContext(Dispatchers.IO) {
            repository.loginUser(name, password)
        }
        withContext(Dispatchers.Main) {
            if (user != null) {
                Log.d("UsersViewModel", user.toString())
                _loginSuccessful.value = Event(true)
            } else {
                _loginSuccessful.value = Event(false)
            }
        }
    }

    fun register(name: String, password: String) = viewModelScope.launch {
        if (name.isBlank() || password.isBlank()) {
            message = "Fields cannot be empty"
            _operationSuccessful.value = Event(false)
            return@launch
        }
        val id = withContext(Dispatchers.IO) {
            repository.registerNewUser(name, password)
        }
        if (id != -1L) {
//            addUser(User(id, name, password))
            _operationSuccessful.value = Event(true)
        } else {
            _operationSuccessful.value = Event(false)
        }
    }

    fun logout(user: User) = viewModelScope.launch {
        val usersLeft = users.value?.size?.minus(1)
        withContext(Dispatchers.IO) {
            repository.logoutUser(user)
        }
        if (usersLeft != null && usersLeft == 0) {
            _noUsersLeft.value = Event(true)
        }
    }
}

