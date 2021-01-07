package com.example.kolkoikrzyzyk.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.kolkoikrzyzyk.Event
import com.example.kolkoikrzyzyk.UserRepository
import com.example.kolkoikrzyzyk.database.AppDatabase
import com.example.kolkoikrzyzyk.model.User
import com.example.kolkoikrzyzyk.notifyObserver
import kotlinx.coroutines.*

class UsersViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(AppDatabase.getInstance(application).userDao())

    private val _users = MutableLiveData<MutableSet<User>>(mutableSetOf())
    val users: LiveData<MutableSet<User>>
        get() = _users

    private val _operationSuccessful = MutableLiveData<Event<Boolean>>()
    val operationSuccessful: LiveData<Event<Boolean>>
        get() = _operationSuccessful

    var message = ""

    private fun addUser(user: User) {
        _users.value?.add(user)
        _users.notifyObserver()
    }

    fun login(name: String, password: String) = viewModelScope.launch {
        val user = withContext(Dispatchers.IO) {
            repository.loginUser(name, password)
        }
        if (user != null) {
            addUser(user)
            _operationSuccessful.value = Event(true)
        } else {
            _operationSuccessful.value = Event(false)
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
            addUser(User(id, name, password))
            _operationSuccessful.value = Event(true)
        } else {
            _operationSuccessful.value = Event(false)
        }
    }

    fun logout(user: User) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.logoutUser(user)
        }
        _users.value?.remove(user)
        _operationSuccessful.value = Event(true)
        _users.notifyObserver()
    }
}

