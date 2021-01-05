package com.example.kolkoikrzyzyk.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kolkoikrzyzyk.model.game.*
import com.example.kolkoikrzyzyk.notifyObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel : ViewModel() {
    private val TAG = "GameViewModel"
    private var isGameWithComputer = false
    private var players = listOf("wojtek")
    var size = 3
    var is3D = true
    private lateinit var game: Game
    private var computer: ComputerPlayer? = null
    private val _boardState = MutableLiveData<Array<Array<Array<Field>>>?>()
    val boardState: LiveData<Array<Array<Array<Field>>>?>
        get() = _boardState
    private val _gameResult = MutableLiveData<GameResult?>()
    val gameResult: MutableLiveData<GameResult?>
        get() = _gameResult
    private var _computerThinking = MutableLiveData(false)
    val computerThinking: LiveData<Boolean>
        get() = _computerThinking


    fun startGame() {
        game = Game(size, is3D)
        _boardState.value = game.gameBoards
        computer = ComputerPlayer(game, PlayerType.Cross)
    }

    fun makeMove(x: Int, y: Int, z: Int) {
        if (game.makeMove(x, y, z)) {
            _boardState.notifyObserver()
        }
        val result = game.checkForWin()
        _gameResult.value = result
        Log.d(TAG, game.moveCount.toString())
        if (result !is GameResult.Pending) {
            return
        }
        computer?.let {
            computerMove(it)
        }
    }

    private fun computerMove(it: ComputerPlayer) =
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _computerThinking.value = true
                withContext(Dispatchers.Default) {
                    it.move()
                }
                _boardState.notifyObserver()
                val result = game.checkForWin()
                _gameResult.value = result
                if (result !is GameResult.Pending) {
                    _computerThinking.value = false
                }
            }
        }
}