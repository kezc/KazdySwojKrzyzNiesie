package com.example.kolkoikrzyzyk.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kolkoikrzyzyk.Event
import com.example.kolkoikrzyzyk.model.User

class SingleGameSettingsViewModel : ViewModel() {
    private val _noughtPlayer = MutableLiveData<User?>()
    val noughtPlayer: LiveData<User?>
        get() = _noughtPlayer
    private val _crossPlayer = MutableLiveData<User?>()
    val crossPlayer: LiveData<User?>
        get() = _crossPlayer
    private val _selectError = MutableLiveData<String>("You need to select two users")
    val selectError: LiveData<String>
        get() = _selectError

    private val _settingsCompleted = MutableLiveData<Event<Pair<User, User>>>()
    val settingsCompleted: LiveData<Event<Pair<User, User>>>
        get() = _settingsCompleted

    var gameSize = 3
    var is3d = false

    fun setNoughtPlayer(user: User) {
        _noughtPlayer.value = user
        checkIfPlayerSelectedCorrectly()
    }

    fun setCrossPlayer(user: User) {
        _crossPlayer.value = user
        checkIfPlayerSelectedCorrectly()
    }

    private fun checkIfPlayerSelectedCorrectly() {
        if (_crossPlayer.value == null || _noughtPlayer.value == null) {
            _selectError.value = "You need to select two users"
        } else if (_noughtPlayer.value == _crossPlayer.value) {
            _selectError.value = "Same user selected twice"
        } else {
            _selectError.value = ""
        }
    }


    fun onPlay() {
        val noughtUser = _noughtPlayer.value
        val crossUser = _crossPlayer.value
        if (noughtUser != null && crossUser != null) {
            _settingsCompleted.value = Event(noughtUser to crossUser)
        }
    }

}
