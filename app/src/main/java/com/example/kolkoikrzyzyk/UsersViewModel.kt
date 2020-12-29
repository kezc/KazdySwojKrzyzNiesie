package com.example.kolkoikrzyzyk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UsersViewModel : ViewModel() {
    private val _users = MutableLiveData<MutableList<String>>(mutableListOf())
    val users: LiveData<MutableList<String>>
        get() = _users

    fun addUser(name: String) {
        _users.value?.add(name)
        _users.notifyObserver()
    }

}

private fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}
